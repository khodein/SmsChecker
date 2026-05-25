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

```
SmsChecker
├── app                   # Точка входа, DI-сборка, навигация
├── framework
│   ├── framework         # Базовые классы, тема, типографика
│   ├── router            # Навигационный контракт
│   └── tools             # Вспомогательные утилиты
├── feature-listening     # Foreground-сервис прослушивания SMS
├── feature-sms           # Доменные модели и хранение SMS
├── feature-email         # Пересылка через SMTP email
├── feature-telegram      # Пересылка в Telegram
└── build-logic           # Convention plugins
```

## Build

```bash
./gradlew assembleDebug
```
