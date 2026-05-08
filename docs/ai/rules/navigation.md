# navigation.md

## Purpose
Это правило описывает, как должна быть устроена навигация в слое presentation

## Scope
Где применяется:
- Presentation
- Правило распространяется на `NavKey`, `Router`, `Route`-Composable и навигационные действия в слое presentation

## Principles
- Навигация должна быть изолирована внутри presentation-слоя и не должна нарушать границы архитектуры
- Каждая фича должна иметь свой навигационный контракт и собственную точку входа в экран
- Переходы между экранами должны выполняться только через `Router` и `NavKey`
- Регистрация экранов должна происходить через `Provider`, который открывает `Route`-Composable фичи

## Rules
- Создавай навигацию только как часть слоя presentation
- Создавай для каждой фичи отдельный интерфейс роутера, например `HomeRouter`
- Размещай навигационный контракт, `NavKey`, реализацию роутера и `Provider` в `presentation` фичи
- Создавай для каждого экрана отдельный `NavKey`
- Делай `NavKey` приватным для фичи, если он не должен использоваться извне
- Используй `@Serializable` для каждого `NavKey`
- Выполняй переходы между экранами только через `Router` и метод `goTo(key: NavKey)`
- Вызывай навигацию из реализации роутера, а не напрямую из `Screen`
- Регистрируй экран через `Router.Provider`
- Открывай в `entry<NavKey>` только `Route`-Composable, а не `Screen`-Composable
- Передавай навигационные действия в `Route`-Composable через лямбды, если экрану нужны переходы в другие фичи
- Получай `ViewModel` внутри `Route` или в точке регистрации экрана в соответствии с правилами фичи
- Не размещай бизнес-логику в навигационных классах
- Не обращайся к `Repository`, `UseCase`, API и базе данных из навигационных классов
- Сохраняй один понятный путь входа в экран через `NavKey`, `Provider` и `Route`

## Do
- Выноси навигационный контракт фичи в отдельный интерфейс, например `HomeRouter`
- Держи `NavKey`, реализацию роутера и `Provider` рядом с навигационным контрактом фичи
- Размещай навигационные сущности фичи в одном файле, например `HomeRouter.kt`, если это соответствует структуре проекта
- Оставляй публичным только интерфейс роутера, а остальные навигационные сущности делай `private`
- Делай `NavKey` приватным, если он используется только внутри фичи
- Используй понятные методы навигации в интерфейсе роутера, например `gotoHomeList()`
- Реализуй переходы в `RouterImpl` через `router.goTo(...)`
- Регистрируй экран через отдельный `Provider`, реализующий `Router.Provider`
- Открывай в `Provider` только `Route`-Composable фичи
- Передавай в `Route` лямбды для навигации, если переход должен вызывать роутер другой фичи
- Передавай роутеры других фич в конструктор `Provider`, если они нужны для настройки navigation-callbacks
- Получай `ViewModel` в точке открытия `Route`, если это соответствует архитектуре фичи
- Держи навигационные классы простыми и ответственными только за переходы
- Следуй правилам `screen.md` для открытия `Route` и `viewmodel.md` для получения `ViewModel`

## Don't
- Не размещай навигацию вне слоя presentation
- Не создавай общий роутер для нескольких фич, если он смешивает их ответственность
- Не вызывай `goTo(...)` напрямую из `Screen`-Composable
- Не открывай `Screen`-Composable напрямую через `entry<NavKey>`
- Не обходи `Route`-Composable при регистрации экрана
- Не создавай `NavKey` без `@Serializable`
- Не делай `NavKey` публичным без необходимости
- Не размещай бизнес-логику в `Router`, `Provider` и `NavKey`
- Не передавай `Repository`, `UseCase`, API и базу данных в навигационные классы
- Не получай данные для экрана внутри навигационных классов
- Не размещай подписку на состояние или работу с `UiState` в `Router` и `Provider`
- Не вызывай роутеры других фич напрямую из `Screen`, если переход можно передать через `Route`-лямбду
- Не открывай экран вне `Router.Provider`, если для него уже определен navigation-поток через `NavKey`
- Не игнорируй правила `screen.md` и `viewmodel.md` при открытии `Route`

## Examples
### ✅ Correct
```text
home/
  presentation/
    HomeRoute.kt
    HomeScreen.kt
    HomeViewModel.kt
    HomeUiState.kt
    HomeRouter.kt
```

```kotlin
interface HomeRouter {
    fun gotoHomeList()
}

@Serializable
private class HomeNavKey : NavKey

private class HomeRouterImpl(
    private val router: Router
) : HomeRouter {

    override fun gotoHomeList() {
        router.goTo(HomeNavKey())
    }
}

private class HomeProviderImpl(
    private val detailsRouter: DetailsRouter
) : Router.Provider {
    override operator fun invoke(): EntryProviderInstaller {
        return {
            entry<HomeNavKey> {
                val viewModel = koinViewModel<HomeViewModel>()
                HomeRoute(
                    viewModel = viewModel,
                    gotoDetails = { id ->
                        detailsRouter.gotoDetails(id)
                    }
                )
            }
        }
    }
}
```

### ❌ Incorrect
```kotlin
@Composable
fun HomeScreen(
    router: Router
) {
    Button(
        onClick = {
            router.goTo(HomeNavKey())
        }
    ) {
        Text("Open")
    }
}
```

```kotlin
private class HomeProviderImpl : Router.Provider {
    override operator fun invoke(): EntryProviderInstaller {
        return {
            entry<HomeNavKey> {
                HomeScreen(
                    state = HomeUiState()
                )
            }
        }
    }
}
```
