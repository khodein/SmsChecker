# AGENTS.md

## Purpose

Этот файл определяет, как AI-агенты должны работать с этим репозиторием.
Перед внесением изменений агент должен прочитать этот документ и следовать указанным здесь правилам, ограничениям и ссылкам на дополнительную документацию.

---

## Project Overview

- Это Android-приложение SmsChecker для локальной проверки и обработки SMS.
- Базовый пакет приложения: `com.sms.checker.forwarder`.
- Проект написан на Kotlin.
- Пользовательский интерфейс построен на Jetpack Compose.
- Архитектурный подход: Clean Architecture с MVI в presentation-слое.
- Код разделен на слои presentation, domain и data.

---

## How to Start

1. Сначала прочитай `docs/ai/context.md`, чтобы понять назначение проекта и общую структуру.
2. Затем изучи правила в `docs/ai/rules/`, чтобы не нарушать архитектурные и командные договоренности.
3. После этого используй инструкции и навыки из `docs/ai/skills/`, если задача требует конкретного сценария работы.
4. Только после изучения документации переходи к анализу кода и внесению изменений.

---

## Tech Stack

- Kotlin — основной язык разработки проекта.
- Jetpack Compose — используется для построения пользовательского интерфейса.
- Koin — используется для внедрения зависимостей.
- Navigation 3 — отвечает за навигацию между экранами.
- Ktor — используется для работы с сетью и API-запросами.
- Room — используется для локального хранения данных на устройстве.
- Coil — используется для загрузки и отображения изображений.

---

## Architecture

- Проект использует Clean Architecture.
- Каждая фича разделена на два Gradle-субмодуля: `api` и `impl`.
- `api` — публичные интерфейсы и контракты фичи (минимальные зависимости: только core-ktx).
- `impl` — реализация фичи: presentation, domain, data; зависит от своего `api`.
- `app` автоматически подключает оба субмодуля через `implementationFeatureModules()`.
- Presentation — слой пользовательского интерфейса на Jetpack Compose и ViewModel.
- Domain — слой бизнес-логики, в котором размещаются UseCase.
- Data — слой работы с данными, включающий Repository, API и локальную базу данных.
- Внедрение зависимостей между слоями и компонентами выполняется через Koin.

---

## Core Rules
- UI-логика не должна содержать бизнес-логику.
- Для хранения состояния в оперативной памяти и работы с состоянием используй ViewModel.
- Каждая ViewModel должна наследоваться от `com.sms.checker.forwarder.framework.BaseViewModel<UiState>`.
- Состояние каждой ViewModel должно наследоваться от `com.sms.checker.forwarder.framework.BaseUiState`.
- Логика частей экрана инкапсулируется в блоках (`BaseBlock`), которые регистрируются в ViewModel через `registerBlocks { add(block, provider) }`.
- ViewModel собирает итоговый `UiState` из состояний блоков в методе `updateViewState()`.
- Пользовательские callbacks хранятся в `Action`-классах блоков и встраиваются в `State` блока как поле `action`.
- Вся бизнес-логика во ViewModel и блоках должна быть написана на чистом Kotlin без использования Android, Compose и других библиотек.
- UI не должен обращаться к Data-слою напрямую.
- Для работы с потоками используй Flow.
- Для запросов к базе данных и API используй `suspend`-функции.
- Не выполняй API- или DB-запросы из Composable-функций.
- Не передавай Android-, Compose- и framework-типы в бизнес-логику.
- `UiState` должен быть immutable.
- Один экран должен иметь одну ViewModel и одно основное состояние экрана.
- Придерживайся подхода Clean Architecture.
- Используй цвета и шрифты из `MaterialTheme`.
- Не добавляй новые цвета и шрифты без разрешения.
- Для навигации используй `Router` и метод `goTo(key: NavKey)`.
- У каждого экрана должен быть свой `NavKey`.
- Версии библиотек и Gradle-плагинов храни только в `gradle/libs.versions.toml`.
- Группы зависимостей подключай через convention plugins из `build-logic`.

---

## Workflow

### Add new feature
1. Создай папку `feature-{name}/` в корне проекта.
2. Создай субмодуль `feature-{name}/api/` с `build.gradle.kts` (plugin: `smschecker.android.feature.api`) и `src/main/AndroidManifest.xml`. Помести сюда публичные интерфейсы фичи.
3. Создай субмодуль `feature-{name}/impl/` с `build.gradle.kts` (plugin: `smschecker.android.feature`; `implementation(project(":feature-{name}:api"))`), `src/main/AndroidManifest.xml` и структурой пакетов.
4. `settings.gradle.kts` подхватит оба субмодуля автоматически; `app` подключит их через `implementationFeatureModules()`.
5. Создай новый `NavKey` в `route/`, если для фичи нужен отдельный экран.
6. Создай `UiState` для состояния экрана в `screen/state/`.
7. Определи логические части экрана и создай для каждой отдельный `Block` в `screen/blocks/{blockName}/`.
8. Создай `ViewModel`, наследуясь от `com.sms.checker.forwarder.framework.BaseViewModel<UiState>`, и зарегистрируй блоки через `registerBlocks { }`.
9. Добавь `UseCase` для бизнес-логики фичи.
10. Создай `Repository`, если фича требует отдельного источника или отдельной логики работы с данными.
11. Добавь domain-модель, если она нужна для бизнес-логики.
12. Добавь data-модели (`request`, `response`, `entity`), если фича работает с API или базой данных.
13. Создай мапперы между data-моделями и domain-моделями.
14. Добавь новые запросы к API или операции с базой данных, если это требуется фичей.
15. Если фича использует локальное хранение, добавь новую таблицу и необходимые методы работы с ней.
16. Подключи зависимости через Koin в `XModule.kt` внутри `impl`.
17. Зарегистрируй `XModule.get()` в `App.kt`.
18. Настрой навигацию через `Router` и `goTo(key: NavKey)`.
19. Создай папки ресурсов для каждой поддерживаемой локали: `src/main/res/values/strings.xml` (English — по умолчанию), `src/main/res/values-ru/strings.xml` (Russian), `src/main/res/values-kk/strings.xml` (Kazakh). Следуй правилам именования и перевода строк из `docs/ai/rules/strings.md`.

---

### Add new non-feature module
1. Создай Gradle-модуль с `build.gradle.kts` и нужными convention plugins.
2. Создай `object XModule` с методом `fun get(): Module` внутри модуля.
3. Зарегистрируй `XModule.get()` в `AppModule.get()` в `App.kt`.
4. Если модуль нужен всем фичам — добавь `project(":module-name")` в `smschecker.android.feature.gradle.kts`.

---

## Constraints
- Не использовать Java. Весь код должен быть написан только на Kotlin.
- Не использовать XML для UI.
- Не размещать бизнес-логику в UI.
- Не использовать Android-, Compose- и framework-зависимости в бизнес-логике.
- Не обходить слои `presentation`, `domain`, `data` и не смешивать их ответственности.
- Не добавлять новые библиотеки без разрешения.
- Не выполнять API- и DB-запросы из Composable-функций.
- Не использовать навигацию вне `Router` и `goTo(key: NavKey)`.
- Не добавлять новые цвета и шрифты без разрешения.
- Не указывать версии зависимостей напрямую в `build.gradle.kts` и `build-logic`.

---

## Commands

### Build
./gradlew assembleDebug

### Test
./gradlew test

### Code quality
./gradlew detekt

### Code quality auto-correct
./gradlew detekt --auto-correct

---

## Code Modification Rules
- Вноси минимально необходимые изменения для решения задачи.
- Не изменяй существующие публичные контракты без необходимости.
- Следуй текущей структуре проекта, именованию и существующим паттернам кода.
- Переиспользуй существующие базовые компоненты, включая `BaseViewModel`, `BaseUiState`, `BaseBlock`, `Router`, `NavKey` и `MaterialTheme`.
- Не создавай новые сущности, если задачу можно решить через существующие.
- Добавляй новые классы, модели, мапперы и репозитории только если они действительно нужны для реализации фичи.
- Не переписывай рабочий код без явной причины.
- Сохраняй границы слоев `presentation`, `domain` и `data` при любых изменениях.
- Если изменения затрагивают Kotlin-код или Gradle Kotlin DSL, перед завершением задачи запускай `./gradlew detekt`, когда это возможно.
- Для безопасного автоисправления форматирования используй `./gradlew detekt --auto-correct`, а затем повторно запускай `./gradlew detekt`.
- Не изменяй `docs/AGENTS.md`, `docs/ai/rules/` и `docs/ai/skills/` без явного разрешения пользователя.

---

## File Structure
- Стандартную структуру файлов внутри feature-модуля смотри в `docs/ai/rules/structure.md`.
- `<feature-name>/` — папка-контейнер фичи, не является Gradle-модулем, содержит `api/` и `impl/`.
- `<feature-name>/api/` — Gradle-субмодуль с публичными интерфейсами фичи; plugin: `smschecker.android.feature.api`.
- `<feature-name>/impl/` — Gradle-субмодуль с реализацией фичи; plugin: `smschecker.android.feature`; зависит от своего `api`.
- `<feature-name>/impl/presentation/` — UI, `ViewModel`, `UiState`, навигация фичи.
- `<feature-name>/impl/domain/` — бизнес-логика, `UseCase`, domain-модели фичи.
- `<feature-name>/impl/data/` — `Repository`, data-модели, мапперы и работа с источниками данных фичи.
- `framework/` — базовые классы и общая инфраструктура проекта.
- `app/.../db/` — application-level `RoomDatabase`, сборка DAO и Entity из feature-модулей.
- `build-logic/` — convention plugins для Gradle-настроек и групп зависимостей.
- `gradle/libs.versions.toml` — единый каталог версий, библиотек и Gradle-плагинов.
- `docs/` — документация проекта.
- `docs/ai/` — контекст, правила и инструкции для AI-агентов.

---

## References
- `docs/ai/context.md` — общий контекст проекта и дополнительные пояснения.
- `docs/ai/rules/` — подробные правила и соглашения проекта.
- `docs/ai/rules/structure.md` — стандартная файловая структура feature-модуля.
- `docs/ai/rules/viewmodel.md` — правила устройства ViewModel и UiState.
- `docs/ai/rules/block.md` — правила устройства Block, его State, Action, Mapper и Widget.
- `docs/ai/rules/screen.md` — правила устройства экрана и UI-компонентов.
- `docs/ai/rules/dependencies.md` — правила добавления и группировки зависимостей.
- `docs/ai/rules/code-quality.md` — правила запуска Detekt и auto-correct.
- `docs/ai/rules/strings.md` — правила именования строковых ресурсов и обязательного перевода на все локали.
- `docs/ai/skills/` — дополнительные инструкции и сценарии работы для AI-агентов.
- При добавлении новых важных документов для AI-агентов этот раздел должен быть расширен.

---

## Notes
- Сначала следуй существующей структуре и паттернам проекта, и только потом предлагай новые подходы.
- Если в проекте уже есть готовая реализация похожей фичи, ориентируйся на нее как на пример.
- Предпочитай консистентность существующего кода вместо лишнего усложнения.
- Если для выполнения задачи не хватает контекста, сначала изучи связанную документацию и существующую реализацию.
- При добавлении новых правил, структуры или документов обновляй `AGENTS.md`.
