<div align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="120" alt="SmsChecker icon" />

  # SmsChecker

  Android-приложение для получения SMS и пересылки на email или в Telegram

  ![Min SDK](https://img.shields.io/badge/minSdk-28-orange?style=flat-square)
  ![Target SDK](https://img.shields.io/badge/targetSdk-37-orange?style=flat-square)
  ![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?style=flat-square&logo=kotlin)
  ![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=flat-square&logo=jetpackcompose)
</div>

---

## Tech Stack

| | Технология | Назначение |
|---|---|---|
| 🧩 | Jetpack Compose | UI |
| 🏗 | Clean Architecture + MVI | Архитектура |
| 💉 | Koin | Dependency Injection |
| 🧭 | Navigation 3 | Навигация |
| 🌐 | Ktor | Сеть |
| 🗄 | Room | Локальная БД |
| ⚙️ | DataStore | Хранение настроек |
| 🖼 | Coil | Загрузка изображений |

## Modules

| Модуль | Назначение |
|---|---|
| `app` | Точка входа, DI-сборка, навигация, application-level `RoomDatabase` |
| `framework` | Базовые контракты presentation: `BaseViewModel`, `UiState`, `UiEvent`, `BaseBlock`, тема, UI-kit |
| `framework/router` | Навигационный контракт: `Router`, `Router.Provider`, `NavTransition` (поверх Navigation 3) |
| `framework/tools` | Служебные утилиты: `ResProvider`, форматтеры времени |
| `feature-listening` | Foreground-сервис прослушивания SMS |
| `feature-sms` | Доменные модели и хранение SMS |
| `feature-email` | Пересылка SMS на email через SMTP |
| `feature-telegram` | Пересылка SMS в Telegram |
| `build-logic` | Convention plugins для Gradle |

## File Structure

```text
SmsChecker/
├── app/                     # Application-модуль
├── framework/               # Базовые контракты + тема + UI-kit (Gradle module)
│   ├── router/              # Навигационный контракт (Gradle submodule)
│   └── tools/               # Служебные утилиты (Gradle submodule)
├── feature-listening/
│   ├── api/                 # Публичный контракт фичи
│   └── impl/                # Реализация фичи
├── feature-sms/
│   ├── api/
│   └── impl/
├── feature-email/
│   ├── api/
│   └── impl/
├── feature-telegram/
│   ├── api/
│   └── impl/
├── build-logic/             # Convention plugins
├── gradle/                  # libs.versions.toml — каталог версий
└── docs/                    # Документация и правила архитектуры
```

Каждая фича — папка-контейнер с двумя Gradle-субмодулями `api` (публичный контракт) и `impl` (реализация). `framework/` сам является Gradle-модулем и содержит два подмодуля `router/` и `tools/`.

## Documentation

Документация хранится в `docs/` и описывает архитектурный шаблон проекта. По ней AI-агент строит архитектуру и пишет код, соответствующий принятым правилам.

| Файл | Назначение |
|---|---|
| [`docs/AGENTS.md`](docs/AGENTS.md) | Контракт для AI-агента: project variables, архитектура, core rules, file structure, references |
| [`docs/ai/context.md`](docs/ai/context.md) | Продуктовый контекст проекта и правила чтения документации |
| [`docs/ai/rules/architecture.md`](docs/ai/rules/architecture.md) | Границы слоёв presentation/domain/data и правила api/impl |
| [`docs/ai/rules/structure.md`](docs/ai/rules/structure.md) | Стандартная файловая структура feature-модуля |
| [`docs/ai/rules/api.md`](docs/ai/rules/api.md) | Что разрешено в `api`-подмодуле фичи |
| [`docs/ai/rules/framework.md`](docs/ai/rules/framework.md) | Содержимое `framework/`, `framework/router/`, `framework/tools/` |
| [`docs/ai/rules/viewmodel.md`](docs/ai/rules/viewmodel.md) | `BaseViewModel<State, Action>`, `UiState`, `UiEvent` |
| [`docs/ai/rules/block.md`](docs/ai/rules/block.md) | `BaseBlock<State, Action, Provider>`, `Mapper`, `Widget` |
| [`docs/ai/rules/screen.md`](docs/ai/rules/screen.md) | Route и Screen, передача `state` и `action` |
| [`docs/ai/rules/navigation.md`](docs/ai/rules/navigation.md) | `Router`, `NavKey`, `Router.Provider` фичи |
| [`docs/ai/rules/usecase.md`](docs/ai/rules/usecase.md) | `UseCase` interface (api) и реализация (impl) |
| [`docs/ai/rules/repository.md`](docs/ai/rules/repository.md) | `Repository` interface (impl/domain) и реализация (impl/data) |
| [`docs/ai/rules/db.md`](docs/ai/rules/db.md) | DAO и Entity в фиче, `RoomDatabase` в `app` |
| [`docs/ai/rules/di.md`](docs/ai/rules/di.md) | Koin-модули фич и `AppModule` |
| [`docs/ai/rules/dependencies.md`](docs/ai/rules/dependencies.md) | Каталог версий и convention plugins |
| [`docs/ai/rules/strings.md`](docs/ai/rules/strings.md) | Именование и локализация строковых ресурсов |

Документация написана как переносимый шаблон с плейсхолдерами (`<project>`, `<root.package>`, `<prefix>`, `<name>`), поэтому её можно перенести в другой Android-проект и поднять архитектуру с нуля.

## Build

```bash
./gradlew assembleDebug
```

## Test

```bash
./gradlew test
```
