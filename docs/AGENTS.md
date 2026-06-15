# AGENTS.md

## Purpose

Этот файл определяет, как AI-агенты должны работать с этим репозиторием.
Перед внесением изменений агент должен прочитать этот документ и следовать указанным здесь правилам, ограничениям и ссылкам на дополнительную документацию.

Документация в `docs/` — переносимый шаблон архитектуры. По ней AI-агент должен:
- понимать целевую архитектуру проекта;
- строить недостающие части (модули, слои, навигацию, DI) с нуля, если их ещё нет;
- писать новый код, не нарушая описанные паттерны.

---

## Project Variables

Перед использованием этого шаблона в конкретном проекте подставь значения вместо плейсхолдеров:

- `<project>` — отображаемое имя проекта (например, `SmsChecker`, `Notes`, `Shop`).
- `<root.package>` — базовый пакет проекта (например, `com.example.app`). Все исходники лежат под этим пакетом.
- `<prefix>` — префикс convention-плагинов в `build-logic/` (например, `app`, `myapp`). Плагины именуются как `<prefix>.android.application`, `<prefix>.android.feature`, `<prefix>.koin` и т.п.
- `App<X>` (`AppTheme`, `AppDatabase`, `AppModule`) — реальные имена в коде проекта; здесь используются как нейтральные.

Все пути, FQN и id плагинов в правилах используют эти плейсхолдеры. Когда правила переносятся в новый проект — раскрой плейсхолдеры под него.

---

## Project Overview

- Это Android-приложение `<project>`.
- Базовый пакет приложения: `<root.package>`.
- Проект написан на Kotlin.
- Пользовательский интерфейс построен на Jetpack Compose.
- Архитектурный подход: Clean Architecture с MVI в presentation-слое.
- Код разделён на слои presentation, domain и data.

---

## How to Start

1. Сначала прочитай `docs/ai/context.md`, чтобы понять продуктовый контекст и общую структуру проекта.
2. Затем изучи правила в `docs/ai/rules/`, чтобы не нарушать архитектурные и командные договоренности.
3. Только после изучения документации переходи к анализу кода и внесению изменений.

---

## Tech Stack

- Kotlin — основной язык разработки проекта.
- Jetpack Compose — построение пользовательского интерфейса.
- Koin — внедрение зависимостей.
- Navigation 3 — навигация между экранами.
- Ktor — сеть и API-запросы.
- Room — локальное хранение данных.
- Coil — загрузка и отображение изображений.

---

## Architecture

- Проект использует Clean Architecture с тремя слоями: presentation, domain, data.
- Базовая инфраструктура расположена в `framework/` и состоит из трёх Gradle-модулей:
    - `framework/` — базовые контракты presentation-слоя: `BaseViewModel`, `UiState`, `UiEvent`,
      `Status`, `Block`, `BlockStore`, тема (`AppTheme`) и общие UI-компоненты (`uikit`).
  - `framework/router/` — навигационный контракт: `Router`, `Router.Provider`, `EntryProviderInstaller`, `NavTransition` (поверх Navigation 3).
  - `framework/tools/` — служебные утилиты (`ResProvider`, форматтеры времени и т.п.).
- Каждая фича — папка-контейнер `feature-<name>/`, разбитая на два Gradle-субмодуля: `api` и `impl`.
  - `api` — публичный контракт фичи: интерфейс роутера (`XRouter`), domain-модели, интерфейсы `UseCase`, domain-исключения. Подключается через convention plugin `<prefix>.android.feature.api` и зависит только от `framework/tools`.
  - `impl` — реализация фичи: `XModule.kt` (Koin), реализация роутера (`XRouterImpl`) и `Router.Provider` (`XProviderImpl`), presentation (Route/Screen/ViewModel/blocks), domain (`XRepository` interface, `XUseCaseImpl`), data (`XRepositoryImpl`, мапперы), `db/` (DAO + Entity). Подключается через `<prefix>.android.feature` и зависит от своего `api`.
- `app` автоматически подключает оба субмодуля каждой фичи через `implementationFeatureModules()`. Application-level `RoomDatabase` (`AppDatabase`) живёт в `app/db/` и собирает все DAO и Entity из feature-модулей.
- Presentation — UI на Jetpack Compose, `ViewModel`, `UiState`, навигация через `Router`.
- Domain — бизнес-логика: `UseCase` (интерфейс в `api`, реализация в `impl`), domain-модели, `Repository` interface.
- Data — `RepositoryImpl`, мапперы, DAO и работа с источниками данных.
- Внедрение зависимостей выполняется через Koin: каждая фича объявляет `object XModule { fun get(): Module }`, который регистрируется в корневом `AppModule.get()` в `app`.

---

## Core Rules
- UI-логика не должна содержать бизнес-логику.
- Для хранения состояния в оперативной памяти и работы с состоянием используй `ViewModel`.
- Каждая `ViewModel` должна наследоваться от `BaseViewModel<State, Action>`.
- Состояние каждой `ViewModel` должно наследоваться от `UiState`.
- Логика частей экрана инкапсулируется в блоках `Block<State, Action, Provider>`, которые
  регистрируются в `ViewModel` через `registerBlocks { add(block) }` или `add(block, provider)`.
- Если блок зависит от внешних callbacks, объявляй интерфейс `Provider` внутри самого Block-класса; `ViewModel` (или другой объект) реализует этот интерфейс и передаётся при `add(block, provider)`.
- `ViewModel` собирает итоговый `UiState` из состояний блоков в методе `updateViewState()` (или через `setState { }`).
- `Action` экрана и блоков — отдельный immutable класс, который выставляется как `val action` у `ViewModel`/`Block` и передаётся в Composable-функции отдельным параметром, а не как поле `state`.
- One-time события (snackbar, навигационные триггеры) идут через `UiEvent` + `BaseViewModel.uiEvent: SharedFlow<UiEvent>` и обрабатываются в `Route`-Composable через `LaunchedEffect`.
- Вся бизнес-логика во `ViewModel`, блоках и `UseCase` должна быть написана на чистом Kotlin без Android, Compose и других UI-зависимостей.
- UI не должен обращаться к Data-слою напрямую — только через `UseCase`.
- Для работы с потоками используй `Flow`/`StateFlow`/`SharedFlow`.
- Для запросов к базе данных и API используй `suspend`-функции.
- Не выполняй API- или DB-запросы из Composable-функций.
- Не передавай Android-, Compose- и framework-типы в бизнес-логику.
- `UiState`, `Action` и состояния блоков должны быть immutable (`val`, `data class`, `@Immutable`).
- Один экран должен иметь одну `ViewModel` и один основной `UiState`.
- Придерживайся подхода Clean Architecture.
- Используй цвета и шрифты из `MaterialTheme` (и проектной обёртки `AppTheme`).
- Не добавляй новые цвета и шрифты без разрешения.
- Для навигации используй интерфейс-роутер фичи (`XRouter`), реализация которого вызывает `Router.goTo(key: NavKey)`.
- У каждого экрана должен быть свой `NavKey`.
- Версии библиотек и Gradle-плагинов храни только в `gradle/libs.versions.toml`.
- Группы зависимостей подключай через convention plugins из `build-logic`.

---

## Constraints
- Не использовать Java. Весь код должен быть написан только на Kotlin.
- Не использовать XML для UI.
- Не размещать бизнес-логику в UI.
- Не использовать Android-, Compose- и framework-зависимости в бизнес-логике.
- Не обходить слои `presentation`, `domain`, `data` и не смешивать их ответственности.
- Не добавлять новые библиотеки без разрешения.
- Не выполнять API- и DB-запросы из Composable-функций.
- Не использовать навигацию вне интерфейса-роутера фичи (`XRouter`) и `Router`.
- Не добавлять новые цвета и шрифты без разрешения.
- Не указывать версии зависимостей напрямую в `build.gradle.kts` и `build-logic`.

---

## Commands

### Build
./gradlew assembleDebug

### Test
./gradlew test

---

## Code Modification Rules
- Вноси минимально необходимые изменения для решения задачи.
- Не изменяй существующие публичные контракты без необходимости.
- Следуй текущей структуре проекта, именованию и существующим паттернам кода.
- Переиспользуй существующие базовые компоненты, включая `BaseViewModel`, `UiState`, `Block`,
  `Router`, `NavKey` и `MaterialTheme`.
- Не создавай новые сущности, если задачу можно решить через существующие.
- Добавляй новые классы, модели, мапперы и репозитории только если они действительно нужны для реализации фичи.
- Не переписывай рабочий код без явной причины.
- Сохраняй границы слоёв `presentation`, `domain` и `data` при любых изменениях.
- Не изменяй `docs/AGENTS.md` и `docs/ai/rules/` без явного разрешения пользователя.

---

## File Structure
- Стандартную структуру файлов внутри feature-модуля смотри в `docs/ai/rules/structure.md`.
- `feature-<name>/` — папка-контейнер фичи, не является Gradle-модулем, содержит `api/` и `impl/`.
- `feature-<name>/api/` — Gradle-субмодуль с публичным контрактом фичи: router interface, domain-модели, `UseCase` interfaces, domain-исключения. Plugin: `<prefix>.android.feature.api`.
- `feature-<name>/impl/` — Gradle-субмодуль с реализацией фичи; plugin: `<prefix>.android.feature`; зависит от своего `api`.
- `feature-<name>/impl/<X>Module.kt` — Koin-модуль фичи (`object XModule { fun get(): Module }`).
- `feature-<name>/impl/router/` — `XRouterImpl`, `XProviderImpl : Router.Provider` (оба `internal`).
- `feature-<name>/impl/presentation/` — `NavKey`, `Route` и `Screen`-Composable, `ViewModel`, `UiState`, `Action`, блоки.
- `feature-<name>/impl/domain/` — `XRepository` interface (internal), `XUseCaseImpl` реализации.
- `feature-<name>/impl/data/` — `XRepositoryImpl`, мапперы и работа с источниками данных.
- `feature-<name>/impl/db/` — `XDao` interface, `XEntity` (Room entity).
- `framework/` — базовые контракты presentation: `BaseViewModel`, `UiState`, `UiEvent`, `Status`,
  `Block`, тема и UI-компоненты.
- `framework/router/` — `Router`, `Router.Provider`, `EntryProviderInstaller`, `NavTransition`.
- `framework/tools/` — служебные утилиты (`ResProvider`, форматтеры).
- `app/db/` — application-level `RoomDatabase` (`AppDatabase`), сборка DAO и Entity из feature-модулей.
- `app/router/` — корневой `RouterImpl`, который агрегирует все `Router.Provider` из фич.
- `build-logic/` — convention plugins для Gradle-настроек и групп зависимостей.
- `gradle/libs.versions.toml` — единый каталог версий, библиотек и Gradle-плагинов.
- `docs/` — документация проекта.
- `docs/ai/` — контекст и правила для AI-агентов.

---

## References
- `docs/ai/context.md` — общий контекст проекта и дополнительные пояснения.
- `docs/ai/rules/` — подробные правила и соглашения проекта.
- `docs/ai/rules/architecture.md` — границы слоёв presentation/domain/data.
- `docs/ai/rules/structure.md` — стандартная файловая структура feature-модуля.
- `docs/ai/rules/api.md` — что разрешено в `api`-подмодуле фичи.
- `docs/ai/rules/framework.md` — содержимое `framework/`, `framework/router/`, `framework/tools/`.
- `docs/ai/rules/viewmodel.md` — правила устройства `ViewModel` и `UiState`.
- `docs/ai/rules/block.md` — правила устройства `Block`, его `State`, `Action`, `Mapper` и `Widget`.
- `docs/ai/rules/screen.md` — правила устройства экрана и UI-компонентов.
- `docs/ai/rules/navigation.md` — `Router`, `NavKey`, `Router.Provider` фичи.
- `docs/ai/rules/usecase.md` — `UseCase` interface (`api`) и реализация (`impl`).
- `docs/ai/rules/repository.md` — `Repository` interface (`impl/domain`) и реализация (`impl/data`).
- `docs/ai/rules/delegate.md` — папка `delegate/` фичи: интерфейс и реализация runtime-делегата.
- `docs/ai/rules/db.md` — DAO и Entity в фиче, `RoomDatabase` в `app`.
- `docs/ai/rules/di.md` — Koin-модули фич и `AppModule`.
- `docs/ai/rules/dependencies.md` — правила добавления и группировки зависимостей.
- `docs/ai/rules/strings.md` — правила именования строковых ресурсов и обязательного перевода на все локали.
- При добавлении новых важных документов для AI-агентов этот раздел должен быть расширен.

---

## Notes
- Сначала следуй существующей структуре и паттернам проекта, и только потом предлагай новые подходы.
- Если в проекте уже есть готовая реализация похожей фичи, ориентируйся на неё как на пример.
- Предпочитай консистентность существующего кода вместо лишнего усложнения.
- Если для выполнения задачи не хватает контекста, сначала изучи связанную документацию и существующую реализацию.
- При добавлении новых правил, структуры или документов обновляй `AGENTS.md`.
