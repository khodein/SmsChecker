# structure.md

## Структура feature-модуля

### Назначение
Определяет стандартную файловую структуру внутри feature-модуля.
Единое соглашение по расположению и именованию файлов делает навигацию по коду предсказуемой, а добавление новых фич — механической операцией.

### Делает
- Описывает расположение всех файлов внутри фичи: что лежит в `api`, что в `impl`.
- Определяет правила видимости (`public` / `internal`) для каждой группы классов.
- Задаёт именование файлов по слоям и по экранам.
- Задаёт вложенность папок внутри `presentation`.

### Не делает
- Не описывает содержимое и логику отдельных файлов — это задача правил `screen.md`, `viewmodel.md`, `block.md`, `navigation.md`, `usecase.md`, `repository.md`, `db.md`, `di.md`.
- Не описывает что лежит в `framework/*` — это задача `framework.md`.

### Базовая схема

Каждая фича — это папка-контейнер `feature-<name>/` (не Gradle-модуль), внутри которой два Gradle-субмодуля: `api` и `impl`.

```text
feature-<name>/                                — папка-контейнер, не Gradle-модуль
  api/                                         — публичный контракт фичи
    build.gradle.kts                           — plugin: <prefix>.android.feature.api
    src/main/AndroidManifest.xml
    src/main/java/<root.package>/feature/<name>/
      router/
        XRouter.kt                             — public interface (нужен, если фича навигируема извне)
      delegate/
        XDelegate.kt                           — public interface (контракт delegate, см. delegate.md)
      domain/
        model/
          XModel.kt                            — public data class (domain-модель)
        usecase/
          XUseCase.kt                          — public interface (контракт UseCase)
        exception/
          XException.kt                        — public sealed class (domain-исключения)
  impl/                                        — реализация фичи
    build.gradle.kts                           — plugin: <prefix>.android.feature; зависит от своего api
    src/main/AndroidManifest.xml
    src/main/res/                              — locale-папки строк (см. strings.md)
    src/main/java/<root.package>/feature/<name>/
      XModule.kt                               — public Koin object XModule
      router/
        XRouterImpl.kt                         — internal class XRouterImpl : XRouter
        XProviderImpl.kt                       — internal class XProviderImpl : Router.Provider
      delegate/
        XDelegateImpl.kt                       — internal class XDelegateImpl : XDelegate (см. delegate.md)
      data/
        XRepositoryImpl.kt                     — internal class XRepositoryImpl : XRepository
        mapper/
          XDataMapper.kt                       — internal class, маппит entity ↔ domain-model
      db/
        XDao.kt                                — public @Dao interface (видим app для AppDatabase)
        entity/
          XEntity.kt                           — public @Entity class (видим app для AppDatabase)
      domain/
        XRepository.kt                         — internal interface XRepository
        usecase/
          XUseCaseImpl.kt                      — internal class XUseCaseImpl : XUseCase
      presentation/
        route/
          <screen>/
            XScreenKey.kt                      — internal @Serializable data class XScreenKey : NavKey
            XScreenRoute.kt                    — @Composable internal fun XScreenRoute(...)
        screen/
          <screen>/
            XScreenScreen.kt                   — @Composable internal fun XScreenScreen(state, action, ...)
            XScreenViewModel.kt                — internal class XScreenViewModel : BaseViewModel<State, Action>
            mapper/
              XScreenMapper.kt                 — internal class
            state/
              XScreenState.kt                  — internal @Immutable data class : UiState
              XScreenAction.kt                 — internal @Immutable data class
              XScreenEvent.kt                  — internal interface : UiEvent (опционально)
            blocks/
              <block>/
                XBlockBlock.kt                 — internal class XBlockBlock : Block<State, Action, Provider>
                mapper/
                  XBlockMapper.kt
                state/
                  XBlockState.kt
                  XBlockAction.kt
                  XBlockEvent.kt               — опционально, если у блока есть собственные one-time события
                widget/
                  XBlockWidget.kt              — @Composable, принимает state и action
```

### Что лежит в `api`

Только то, что фича выставляет наружу — другим фичам или `app`:

- `router/XRouter.kt` — навигационный контракт фичи (методы вида `gotoXList()`, `gotoXDetail(id)`).
- `delegate/*.kt` — публичные интерфейсы делегатов фичи, через которые соседние фичи или `app`
  подключаются к её рантайму (см. `delegate.md`).
- `domain/model/*.kt` — domain-модели, если они нужны соседним фичам или `app` (как входные/выходные параметры `UseCase`).
- `domain/usecase/*.kt` — интерфейсы `UseCase`, которые могут использоваться другими фичами.
- `domain/exception/*.kt` — domain-исключения, которые соседние фичи могут перехватывать.

В `api` **не должно быть**: `RepositoryImpl`, `Repository` interface, `DAO`, `Entity`, `ViewModel`, `UiState`, мапперов, Composable-функций, Koin-модулей.

### Что лежит в `impl`

Всё остальное: реализация контрактов из `api` плюс presentation-слой.

- `XModule.kt` — Koin-модуль фичи (`object XModule { fun get(): Module }`). **public**, так как вызывается из `AppModule` в `app`.
- `router/XRouterImpl.kt` — реализация `XRouter`, использует `Router` из `framework/router`.
- `router/XProviderImpl.kt` — `Router.Provider`, регистрирует `entry<XScreenKey>` фичи.
- `delegate/*.kt` — реализации делегатов фичи (`XDelegateImpl`, вспомогательные `Receiver`,
  `Service`, `Mapper`-ы только для делегата). См. `delegate.md`.
- `data/XRepositoryImpl.kt` — реализация `XRepository`, использует `XDao` и/или внешние API.
- `data/mapper/XDataMapper.kt` — мапперы между `XEntity`/response/request и domain-моделями.
- `db/XDao.kt` — Room `@Dao interface` (только декларация запросов). **public**, так как `AppDatabase` в `app` его агрегирует.
- `db/entity/XEntity.kt` — Room `@Entity`. **public**, так как `AppDatabase` его агрегирует.
- `domain/XRepository.kt` — `internal interface`, описывает контракт data-слоя для domain.
- `domain/usecase/XUseCaseImpl.kt` — реализация `XUseCase`, использует `XRepository`.
- `presentation/route/<screen>/XScreenKey.kt` — `@Serializable internal data class XScreenKey : NavKey`. Помечается `@Serializable`, поскольку Navigation 3 сериализует state.
- `presentation/route/<screen>/XScreenRoute.kt` — Composable, который получает `ViewModel` через Koin, подписывается на `viewState` и `uiEvent`, передаёт `state` и `action` в `XScreenScreen`.
- `presentation/screen/<screen>/XScreenViewModel.kt` — `internal class XScreenViewModel(...) : BaseViewModel<XScreenState, XScreenAction>()`.
- `presentation/screen/<screen>/XScreenScreen.kt` — чистый Composable-экран, принимает `state` и `action` параметрами.
- `presentation/screen/<screen>/state/` — `XScreenState : UiState`, `XScreenAction`, опционально `XScreenEvent : UiEvent`.
- `presentation/screen/<screen>/mapper/XScreenMapper.kt` — преобразует domain-модель или другое в `UiState` либо в `UiEvent`.
- `presentation/screen/<screen>/blocks/<block>/` — самостоятельный кусок экрана со своими `Block`, `State`, `Action`, `Mapper`, опционально `Event`, и `Widget` (Composable).

### api vs impl

- `api` — только публичные интерфейсы, domain-модели, исключения, навигационный контракт. Содержимое минимально и стабильно.
- `impl` зависит от своего `api` через `implementation(project(":feature-<name>:api"))`.
- `app` подключает оба субмодуля автоматически через `implementationFeatureModules()` — добавлять `include` или `implementation(project(...))` руками не нужно.
- Другие feature-модули зависят только от `:feature-<name>:api`, никогда от `:feature-<name>:impl`.

### Видимость

| Где                         | Что                                                                                                                                                                                                                        | Модификатор                             |
|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------|
| `api/**`                    | всё содержимое (router interface, delegate interfaces, models, usecase interfaces, exceptions)                                                                                                                             | `public` (по умолчанию)                 |
| `impl/XModule.kt`           | `object XModule`                                                                                                                                                                                                           | `public` (вызывается из `AppModule`)    |
| `impl/db/XDao.kt`           | `@Dao interface XDao`                                                                                                                                                                                                      | `public` (агрегируется в `AppDatabase`) |
| `impl/db/entity/XEntity.kt` | `@Entity class XEntity`                                                                                                                                                                                                    | `public` (агрегируется в `AppDatabase`) |
| `impl/**` всё остальное     | `RepositoryImpl`, `Repository` interface, `UseCaseImpl`, `XRouterImpl`, `XProviderImpl`, `XDelegateImpl`, `ViewModel`, `Screen`, `Route`, `NavKey`, `Block`, `State`, `Action`, `Event`, `Mapper`, `Widget`, `Data*Mapper` | `internal`                              |

Старый паттерн «`private` classes внутри `object XModule`» **не используется**. Классы располагаются в собственных файлах и помечаются `internal`.

### Именование файлов

`X` ниже — это PascalCase-имя фичи (например, `Home`, `Notes`, `Cart`). `<name>` — то же самое в kebab-case для имени папки (`home`, `notes`, `cart`). `<screen>` и `<block>` — короткие имена в lowercase (`list`, `detail`, `edit`, `toolbar`, `bottombar`).

- `XModule.kt` — `Feature` + `Module`, например `HomeModule.kt`.
- `XRouter.kt` (api) / `XRouterImpl.kt`, `XProviderImpl.kt` (impl) — навигация фичи.
- `XDelegate.kt` (api) / `XDelegateImpl.kt` (impl) — пара контракт + реализация делегата. Имя
  обязательно заканчивается на `Delegate`. См. `delegate.md`.
- `XRepository.kt` (impl/domain) / `XRepositoryImpl.kt` (impl/data) — пара контракт + реализация.
- `XUseCase.kt` (api) / `XUseCaseImpl.kt` (impl) — пара контракт + реализация. Имя начинается с глагола: `GetHomeUseCase`, `SaveNoteUseCase`, `DeleteCartItemUseCase`.
- `XModel.kt` (api) — domain-модель. Имя обязательно заканчивается на `Model`: `HomeModel`, `NoteModel`, `CartItemModel`.
- `XException.kt` (api) — `sealed class XException : Exception()`, варианты — `data class` / `object`.
- `XDao.kt`, `XEntity.kt` — Room-объекты в `impl/db/`.
- `XDataMapper.kt` — маппер data-слоя.
- `X<Screen>Key.kt` — `Feature` + `Screen` + `Key`, например `HomeListKey.kt`.
- `X<Screen>Route.kt`, `X<Screen>Screen.kt`, `X<Screen>ViewModel.kt`, `X<Screen>State.kt`, `X<Screen>Action.kt`, `X<Screen>Event.kt`, `X<Screen>Mapper.kt` — пер-screen файлы.
- `X<Block>Block.kt`, `X<Block>State.kt`, `X<Block>Action.kt`, `X<Block>Event.kt`, `X<Block>Mapper.kt`, `X<Block>Widget.kt` — пер-block файлы.

### Несколько экранов в одной фиче

Если у фичи несколько экранов, каждый экран — отдельная папка внутри `presentation/route/` и `presentation/screen/`. Общие domain/data слои разделяются между экранами.

```text
impl/src/main/java/<root.package>/feature/home/
  presentation/
    route/
      list/   HomeListKey.kt   HomeListRoute.kt
      detail/ HomeDetailKey.kt HomeDetailRoute.kt
    screen/
      list/   HomeListScreen.kt   HomeListViewModel.kt   state/...   blocks/...
      detail/ HomeDetailScreen.kt HomeDetailViewModel.kt state/...   blocks/...
```

### Пример

Для фичи `home` с одним экраном `list`:

```text
feature-home/
  api/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/java/<root.package>/feature/home/
      router/HomeRouter.kt
      domain/
        model/HomeModel.kt
        usecase/GetHomeUseCase.kt
        exception/HomeException.kt
  impl/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/res/values/strings.xml
    src/main/res/values-ru/strings.xml
    src/main/res/values-kk/strings.xml
    src/main/java/<root.package>/feature/home/
      HomeModule.kt
      router/
        HomeRouterImpl.kt
        HomeProviderImpl.kt
      data/
        HomeRepositoryImpl.kt
        mapper/HomeDataMapper.kt
      db/
        HomeDao.kt
        entity/HomeEntity.kt
      domain/
        HomeRepository.kt
        usecase/GetHomeUseCaseImpl.kt
      presentation/
        route/
          list/
            HomeListKey.kt
            HomeListRoute.kt
        screen/
          list/
            HomeListScreen.kt
            HomeListViewModel.kt
            mapper/HomeListMapper.kt
            state/
              HomeListState.kt
              HomeListAction.kt
              HomeListEvent.kt
            blocks/
              toolbar/
                HomeToolbarBlock.kt
                mapper/HomeToolbarMapper.kt
                state/
                  HomeToolbarState.kt
                  HomeToolbarAction.kt
                widget/HomeToolbarWidget.kt
              bottombar/
                HomeBottomBarBlock.kt
                mapper/HomeBottomBarMapper.kt
                state/
                  HomeBottomBarState.kt
                  HomeBottomBarAction.kt
                  HomeBottomBarEvent.kt
                widget/HomeBottomBarWidget.kt
```
