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

Каждый feature-модуль — это папка без собственного `build.gradle.kts`, внутри которой два Gradle-субмодуля:

```text
feature-x/                            — папка-контейнер, не Gradle-модуль
  api/                                — публичные контракты фичи
    build.gradle.kts                  — plugin: smschecker.android.feature.api
    src/main/AndroidManifest.xml
    src/main/java/.../feature/x/
      XRouter.kt                      — интерфейс роутера (если нужна навигация извне)
      (другие публичные интерфейсы)
  impl/                               — реализация фичи
    build.gradle.kts                  — plugin: smschecker.android.feature; depends on :feature-x:api
    src/main/java/.../feature/x/
      XModule.kt                      — Koin DI
      presentation/
        screen/
          {screenName}/               — папка на каждый экран (list, detail, edit и т.д.)
            route/
              XScreenNameKey.kt       — NavKey экрана
              XScreenNameRoute.kt     — Route composable
            screen/
              state/
                XScreenNameState.kt   — UiState экрана
                XScreenNameItemState.kt — UI-модель элемента (опционально)
                XScreenNameAction.kt  — Action экрана (опционально)
              mapper/
                XScreenNameMapper.kt  — маппер экрана (опционально)
              blocks/                 — блоки — самостоятельные части экрана
                {blockName}/
                  XBlockNameBlock.kt  — логика блока
                  state/
                    XBlockNameState.kt
                    XBlockNameAction.kt
                  mapper/
                    XBlockNameMapper.kt
                  widget/
                    XBlockNameWidget.kt
              XScreenNameScreen.kt    — Screen composable
              XScreenNameViewModel.kt — ViewModel
      domain/
        XUseCase.kt
        XModel.kt
        XRepository.kt               — интерфейс репозитория
      data/
        XRepositoryImpl.kt           — реализация репозитория
```

### api vs impl

- `api` — только публичные интерфейсы и контракты, которые нужны другим модулям или `app`
- `impl` зависит от своего `api` через `implementation(project(":feature-x:api"))`
- `app` подключает оба субмодуля автоматически через `implementationFeatureModules()`
- Содержимое `api` должно быть минимальным — только то, что реально нужно снаружи

### Видимость

- `XModule` и публичные интерфейсы в `api` — `public`, доступны из других модулей
- Всё остальное в `impl` — `internal`
- Классы, вложенные внутрь `object XModule` (`RouterImpl`, `ProviderImpl`) — `private`

### Именование файлов
- `{screenName}` — название экрана строчными буквами: `list`, `detail`, `edit`
- `{blockName}` — название блока строчными буквами: `toolbar`, `bottombar`, `listening`
- `XModule.kt` — `Feature` + `Module`, например `ListeningModule.kt`
- `XScreenNameKey.kt` — `Feature` + `ScreenName` + `Key`, например `ListeningListKey.kt`
- `XScreenNameState.kt` — `Feature` + `ScreenName` + `State`, например `ListeningListState.kt`
- `XScreenNameRoute.kt` — `Feature` + `ScreenName` + `Route`, например `ListeningListRoute.kt`
- `XScreenNameScreen.kt` — `Feature` + `ScreenName` + `Screen`, например `ListeningListScreen.kt`
- `XScreenNameViewModel.kt` — `Feature` + `ScreenName` + `ViewModel`, например `ListeningListViewModel.kt`
- `XBlockNameBlock.kt` — `Feature` + `BlockName` + `Block`, например `ListeningToolbarBlock.kt`
- `XBlockNameState.kt` — `Feature` + `BlockName` + `State`, например `ListeningToolbarState.kt`
- `XBlockNameAction.kt` — `Feature` + `BlockName` + `Action`, например `ListeningToolbarAction.kt`
- `XBlockNameMapper.kt` — `Feature` + `BlockName` + `Mapper`, например `ListeningToolbarMapper.kt`
- `XBlockNameWidget.kt` — `Feature` + `BlockName` + `Widget`, например `ListeningToolbarWidget.kt`

### Пример

```text
feature-listening/
  api/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/java/.../feature/listening/
      ListeningRouter.kt              — интерфейс роутера (публичный контракт)
  impl/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/java/.../feature/listening/
      ListeningModule.kt
      presentation/
        screen/
          list/
            route/
              ListeningListKey.kt
              ListeningListRoute.kt
            screen/
              state/
                ListeningListState.kt
                ListeningListItemState.kt
              blocks/
                toolbar/
                  ListeningToolbarBlock.kt
                  state/
                    ListeningToolbarState.kt
                    ListeningToolbarAction.kt
                  mapper/
                    ListeningToolbarMapper.kt
                  widget/
                    ListeningToolbarWidget.kt
                listening/
                  ListeningBlock.kt
                  state/
                    ListeningState.kt
                    ListeningAction.kt
                  mapper/
                    ListeningMapper.kt
                  widget/
                    ListeningWidget.kt
              ListeningListScreen.kt
              ListeningListViewModel.kt
      domain/
        ...
      data/
        ...
```