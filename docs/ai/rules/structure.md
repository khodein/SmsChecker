# structure.md

## Структура feature-модуля

### Назначение
Определяет стандартную файловую структуру внутри feature-модуля.
Единое соглашение по расположению и именованию файлов делает навигацию по коду предсказуемой.

### Делает
- Описывает расположение всех файлов внутри фичи
- Определяет именование файлов по слоям
- Задаёт вложенность папок внутри `presentation`

### Не делает
- Не описывает содержимое файлов — это задача правил `screen.md`, `viewmodel.md`, `navigation.md`, `di.md`

### Структура

```text
feature-x/
  XModule.kt                          — Koin DI, Router interface, RouterImpl, ProviderImpl
  presentation/
    screen/
      {screenName}/                   — папка на каждый экран (list, detail, edit и т.д.)
        management/
          key/
            XScreenNameKey.kt         — NavKey экрана
          screen/
            XScreenNameState.kt       — UiState экрана
            XScreenNameItem.kt        — UI-модель элемента (опционально)
        route/
          XScreenNameRoute.kt         — Route composable
        screen/
          widget/                     — UI-компоненты экрана (опционально)
            XWidget.kt
          XScreenNameScreen.kt        — Screen composable
          XScreenNameViewModel.kt     — ViewModel
  domain/
    XUseCase.kt
    XModel.kt
    XRepository.kt                    — интерфейс репозитория
  data/
    XRepositoryImpl.kt                — реализация репозитория
```

### Видимость

- `XModule` и `XRouter` — публичные (`public`), доступны из других модулей
- Все остальные классы и функции внутри фичи — `internal`
- Классы, вложенные внутрь `object XModule` (`RouterImpl`, `ProviderImpl`, `NavKey`) — `private`

### Именование файлов
- `{screenName}` — название экрана строчными буквами: `list`, `detail`, `edit`
- `XModule.kt` — `Feature` + `Module`, например `DevModule.kt`
- `XScreenNameKey.kt` — `Feature` + `ScreenName` + `Key`, например `DevListKey.kt`
- `XScreenNameState.kt` — `Feature` + `ScreenName` + `State`, например `DevListState.kt`
- `XScreenNameRoute.kt` — `Feature` + `ScreenName` + `Route`, например `DevListRoute.kt`
- `XScreenNameScreen.kt` — `Feature` + `ScreenName` + `Screen`, например `DevListScreen.kt`
- `XScreenNameViewModel.kt` — `Feature` + `ScreenName` + `ViewModel`, например `DevListViewModel.kt`

### Пример

```text
feature-dev/
  DevModule.kt
  presentation/
    screen/
      list/
        management/
          key/
            DevListKey.kt
          screen/
            DevListState.kt
            DevListItem.kt
        route/
          DevListRoute.kt
        screen/
          widget/
            DevMenuWidget.kt
          DevListScreen.kt
          DevListViewModel.kt
```