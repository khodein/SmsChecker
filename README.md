# SmsChecker

Android-приложение для локальной проверки и обработки SMS.

## Tech Stack

- Kotlin
- Jetpack Compose
- Clean Architecture + MVI
- Koin — DI
- Navigation 3 — навигация
- Ktor — сеть
- Room — локальная БД
- Coil — загрузка изображений

## Modules

| Модуль | Назначение |
|--------|-----------|
| `app` | Точка входа, DI-сборка, навигация |
| `framework` | Базовые классы, тема, типографика |
| `router` | Навигационный контракт |
| `tools` | Вспомогательные утилиты |
| `feature-dev` | Dev-экран для отладки |
| `feature-listening` | Прослушивание SMS |
| `feature-email` | Пересылка на email |
| `feature-telegram` | Пересылка в Telegram |
| `build-logic` | Convention plugins |

## Build

```bash
./gradlew assembleDebug
```

## Requirements

- Android Studio Narwhal+
- minSdk 28
- targetSdk 37
