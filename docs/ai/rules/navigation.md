# navigation.md

## Purpose
Это правило описывает, как должна быть устроена навигация: контракт `XRouter` фичи, её `Router.Provider` и `NavKey`, а также взаимодействие с корневым `Router` из `framework/router`.

## Scope
Где применяется:
- Presentation
- Правило распространяется на `NavKey`, `XRouter`, `XRouterImpl`, `XProviderImpl`, `Route`-Composable и навигационные действия.

## Principles
- Каждая фича имеет свой публичный контракт навигации — interface `XRouter` в `api`.
- Реализация контракта (`XRouterImpl`) и регистрация экранов фичи (`XProviderImpl : Router.Provider`) лежат в `impl/router/` как `internal class`.
- `NavKey` каждого экрана живёт в `impl/presentation/route/<screen>/` как `internal @Serializable data class` (или `internal @Serializable object`, если параметров нет).
- Корневой `Router` (`framework/router`) хранит back stack и не знает про конкретные фичи.
- `app` собирает все `Router.Provider`-ы фич через Koin (`getAll<Router.Provider>()`) и подключает их к `NavDisplay` в `MainActivity`.

## Базовый контракт

`framework/router` предоставляет:

```kotlin
typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

interface Router {
    fun getBackStack(): List<NavKey>
    fun goTo(key: NavKey)
    fun goBack()

    interface Provider {
        operator fun invoke(): EntryProviderInstaller
    }
}
```

Корневой `RouterImpl` (живёт в `app/router/`) — единственная реализация `Router` в проекте. Все фичи получают его в `XRouterImpl` через Koin.

## Структура навигации фичи

```text
feature-<name>/
  api/src/main/java/<root.package>/feature/<name>/
    router/XRouter.kt           ← public interface
  impl/src/main/java/<root.package>/feature/<name>/
    XModule.kt                  ← регистрирует XRouterImpl и XProviderImpl
    router/
      XRouterImpl.kt            ← internal class XRouterImpl : XRouter
      XProviderImpl.kt          ← internal class XProviderImpl : Router.Provider
    presentation/route/
      <screen>/
        XScreenKey.kt           ← internal @Serializable data class XScreenKey : NavKey
        XScreenRoute.kt         ← @Composable internal fun XScreenRoute(...)
```

## Rules
- Создавай интерфейс `XRouter` в `api/router/XRouter.kt`. Он `public`.
- Описывай методы `XRouter` в терминах действий пользователя: `gotoXList()`, `gotoXDetail(id: Long)`, `gotoXEdit(id: Long? = null)`. Имя — `goto<Screen>`.
- Создавай `XRouterImpl` в `impl/router/XRouterImpl.kt` как `internal class`, принимающий корневой `Router` через конструктор. В методах создавай нужный `NavKey` и вызывай `router.goTo(key)` или `router.goBack()`.
- Создавай `XProviderImpl` в `impl/router/XProviderImpl.kt` как `internal class XProviderImpl : Router.Provider`. Возвращай из `invoke()` лямбду, где регистрируешь `entry<XScreenKey>` для каждого экрана фичи.
- Создавай `XScreenKey` в `impl/presentation/route/<screen>/XScreenKey.kt` как `internal @Serializable data class XScreenKey(...) : NavKey` (или `internal @Serializable object XScreenKey : NavKey`, если параметры не нужны).
- Помечай каждый `NavKey` аннотацией `@Serializable` — Navigation 3 сериализует back stack.
- Регистрируй `XRouterImpl` и `XProviderImpl` в `XModule.get()`:
  - `singleOf(::XRouterImpl) bind XRouter::class`
  - `singleOf(::XProviderImpl) bind Router.Provider::class`
- В `entry<XScreenKey>` внутри `XProviderImpl.invoke()` получай `ViewModel` через `koinViewModel<XScreenViewModel>()`. Если ключ несёт параметры, передавай их во `ViewModel` (например, через `setX(key.id)` или конструкторно через `parametersOf`).
- В `entry<XScreenKey>` вызывай только `XScreenRoute(...)`, а не `XScreenScreen(...)`.
- Если фиче нужно вызывать переходы к экранам другой фичи, инжекти её `XRouter` через конструктор `XRouterImpl` или соответствующего блока — никогда напрямую `Router` других фич.
- Для навигации из блока вызывай метод собственного `XRouter`-а фичи или `XRouter` соседней фичи, инжектированный в блок.
- Корневой `Router` (`framework/router`) и его `RouterImpl` живут только в `app/router/`. Не дублируй их в фичах.

## Do
- Размещай публичный контракт навигации фичи в `api/router/XRouter.kt`.
- Делай `XRouterImpl` и `XProviderImpl` `internal class` в `impl/router/`.
- Делай `XScreenKey` `internal @Serializable data class` (или `object`) в `impl/presentation/route/<screen>/`.
- Называй методы `XRouter` по шаблону `goto<Screen>(...)`.
- Возвращай из `XProviderImpl.invoke()` лямбду, регистрирующую все экраны фичи через `entry<XKey> { koinViewModel<XViewModel>().let { vm -> XRoute(viewModel = vm) } }`.
- Регистрируй `singleOf(::XProviderImpl) bind Router.Provider::class` в `XModule`, чтобы `app` подхватил его через `getAll<Router.Provider>()`.
- Передавай параметры экрана через свойства `NavKey` (id, query, режим открытия) — они сериализуются.
- Используй `Router.goBack()` для возврата.
- Если переход должен вызывать роутер другой фичи, инжекти его `XRouter` в нужный класс (`XRouterImpl` или блок).

## Don't
- Не размещай `XRouter` interface в `impl` — он должен быть доступен другим фичам через `api`.
- Не размещай `NavKey`, `XRouterImpl`, `XProviderImpl` как `private` classes внутри `object XModule` — это устаревший паттерн. Используй отдельные файлы с модификатором `internal`.
- Не размещай `NavKey` в `api` — он принадлежит реализации фичи и не должен быть видим снаружи.
- Не создавай `NavKey` без `@Serializable`.
- Не открывай `XScreenScreen` напрямую через `entry<XScreenKey>` — только `XScreenRoute`, иначе не будет подписки на `viewState` / `uiEvent`.
- Не вызывай `router.goTo(...)` из `Screen`, `Widget` или `ViewModel` напрямую — навигация инициируется через `XRouter` фичи.
- Не передавай корневой `Router` в `Screen`, `Widget` или domain/data слои.
- Не создавай общий роутер на несколько фич — каждая фича имеет свой `XRouter`.
- Не размещай бизнес-логику в `XRouterImpl` и `XProviderImpl`.
- Не обращайся к `Repository`, `UseCase`, API из навигационных классов.
- Не подключай `impl` другой фичи ради её `XRouter` — `XRouter` уже доступен через её `api`.

## Examples

### ✅ Correct — фича с одним экраном и параметром

`api/router/XRouter.kt`:
```kotlin
interface XRouter {
    fun gotoXDetail(id: Long)
}
```

`impl/router/XRouterImpl.kt`:
```kotlin
internal class XRouterImpl(
    private val router: Router,
) : XRouter {
    override fun gotoXDetail(id: Long) {
        router.goTo(XDetailKey(id = id))
    }
}
```

`impl/presentation/route/detail/XDetailKey.kt`:
```kotlin
@Serializable
internal data class XDetailKey(
    val id: Long,
) : NavKey
```

`impl/presentation/route/detail/XDetailRoute.kt`:
```kotlin
@Composable
internal fun XDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: XDetailViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    XDetailScreen(modifier = modifier, state = state, action = action)
}
```

`impl/router/XProviderImpl.kt`:
```kotlin
internal class XProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<XDetailKey> { key ->
            val viewModel = koinViewModel<XDetailViewModel>().also {
                it.setId(key.id)
            }
            XDetailRoute(viewModel = viewModel)
        }
    }
}
```

`impl/XModule.kt`:
```kotlin
object XModule {
    fun get() = module {
        // navigation
        singleOf(::XRouterImpl) bind XRouter::class
        singleOf(::XProviderImpl) bind Router.Provider::class

        // presentation
        viewModelOf(::XDetailViewModel)

        // ... остальные регистрации фичи
    }
}
```

### ✅ Correct — фича с двумя экранами

```kotlin
// api/router/XRouter.kt
interface XRouter {
    fun gotoXList()
    fun gotoXEdit(id: Long? = null)
}

// impl/router/XRouterImpl.kt
internal class XRouterImpl(
    private val router: Router,
) : XRouter {
    override fun gotoXList() = router.goTo(XListKey)
    override fun gotoXEdit(id: Long?) = router.goTo(XEditKey(id = id))
}

// impl/presentation/route/list/XListKey.kt
@Serializable
internal object XListKey : NavKey

// impl/presentation/route/edit/XEditKey.kt
@Serializable
internal data class XEditKey(val id: Long?) : NavKey

// impl/router/XProviderImpl.kt
internal class XProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<XListKey> {
            val viewModel = koinViewModel<XListViewModel>()
            XListRoute(viewModel = viewModel)
        }
        entry<XEditKey> { key ->
            val viewModel = koinViewModel<XEditViewModel>().also {
                it.setId(key.id)
            }
            XEditRoute(viewModel = viewModel)
        }
    }
}
```

### ✅ Correct — переход в другую фичу из блока

```kotlin
internal class XListItemBlock(
    private val yRouter: YRouter,                  // инжектим контракт соседней фичи (из её api)
) : BaseBlock<XListItemState, XListItemAction, Unit>() {

    override val action = XListItemAction(
        onClickItem = ::onClickItem,
    )

    private fun onClickItem(id: Long) {
        yRouter.gotoYDetail(id)                    // вызываем YRouter, не Router напрямую
    }
    // ...
}
```

### ❌ Incorrect

```kotlin
// 1. NavKey без @Serializable
internal data class XDetailKey(val id: Long) : NavKey      // ❌ Navigation 3 потребует @Serializable

// 2. NavKey вынесен в api
// api/router/XDetailKey.kt
@Serializable
data class XDetailKey(val id: Long) : NavKey                // ❌ ключ принадлежит impl

// 3. private classes внутри object XModule (устаревший паттерн)
object XModule {
    fun get() = module {
        singleOf(::XRouterImpl) bind XRouter::class
        singleOf(::XProviderImpl) bind Router.Provider::class
    }

    private class XRouterImpl(private val router: Router) : XRouter { ... }   // ❌ выноси в отдельный файл, делай internal
    private class XProviderImpl : Router.Provider { ... }                      // ❌
    @Serializable private object XDetailKey : NavKey                           // ❌
}

// 4. Открытие XScreenScreen вместо XScreenRoute
entry<XDetailKey> { key ->
    XDetailScreen(...)                                       // ❌ нужен XDetailRoute, иначе нет подписки на ViewModel
}

// 5. Router напрямую из Screen
@Composable
fun XDetailScreen(router: Router) {                          // ❌ навигация только через XRouter фичи в блоке
    Button(onClick = { router.goTo(YDetailKey(1)) }) { Text("Open") }
}

// 6. Подключение impl чужой фичи ради её Router
dependencies {
    implementation(project(":feature-y:impl"))               // ❌ YRouter доступен через :feature-y:api
}
```
