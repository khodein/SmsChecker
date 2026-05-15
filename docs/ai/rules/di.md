# di.md

## Purpose
Это правило описывает, как должно быть устроено внедрение зависимостей в проекте через Koin

## Scope
Где применяется:
- All
- Правило распространяется на объявление зависимостей, Koin-модули и получение зависимостей в слоях проекта

## Principles
- Внедрение зависимостей в проекте должно быть организовано только через Koin
- Зависимости должны регистрироваться и получаться только в рамках допустимых связей между `presentation`, `domain` и `data`
- Каждый компонент должен получать только те зависимости, которые соответствуют его ответственности
- Создание `ViewModel`, `UseCase`, `Repository` и других компонентов должно происходить через DI, а не вручную
- Конфигурация DI должна оставаться простой, предсказуемой и удобной для поддержки

## Rules
- Размещай конфигурацию DI для каждой фичи в файле `feature/FeatureNameModule.kt`
- Объявляй зависимости фичи внутри `object XModule` с методом `fun get(): Module`
- Создавай отдельный модуль для каждой фичи
- Регистрируй `ViewModel`, `UseCase`, `Repository` и другие зависимости фичи только внутри `XModule.get()`
- Регистрируй `Router` вместе с остальными зависимостями фичи внутри `XModule.get()`
- Регистрируй `ViewModel` через Koin DSL `viewModelOf`
- Используй `singleOf`, `factoryOf`, `single`, `factory` и другие Koin-регистрации в соответствии с жизненным циклом зависимости
- Сохраняй зависимости фичи внутри ее собственной области ответственности
- Регистрируй зависимости в модуле в соответствии со слоями `presentation`, `domain` и `data`
- Получай зависимости только через Koin, а не через ручное создание экземпляров
- Не регистрируй зависимости фичи вне `FeatureNameModule.kt` без явной причины
- Не смешивай зависимости разных фич в одном модуле
- Не нарушай архитектурные границы при регистрации зависимостей

## Do
- Создавай для каждой фичи отдельный файл `FeatureNameModule.kt`
- Объявляй зависимости фичи внутри одного `object XModule { fun get() = module { ... } }`
- Регистрируй `ViewModel` через `viewModelOf`
- Используй `factoryOf` и `singleOf`, если они подходят для регистрации зависимости
- Связывай интерфейсы с реализациями явно, если у компонента есть контракт
- Группируй регистрации внутри модуля по слоям `presentation`, `domain` и `data`
- Держи модуль фичи компактным и понятным по структуре
- Подключай в модуль только зависимости, которые действительно нужны фиче
- Используй `get()` только для тех зависимостей, которые уже зарегистрированы в Koin
- Регистрируй `UseCase`, `Repository`, `Router` и другие компоненты в соответствии с их жизненным циклом
- Регистрируй `Router` внутри `XModule.get()` вместе с другими зависимостями фичи
- После создания нового модуля добавляй `XModule.get()` в `AppModule.get()` в `App.kt`
- Если модуль должен быть доступен всем фичам — добавляй `project(":module-name")` в `smschecker.android.feature.gradle.kts`
- Следуй архитектурным границам проекта при объявлении зависимостей
- Следуй структуре файлов и правилам видимости из `docs/ai/rules/structure.md`

## Don't
- Не создавай зависимости вручную в точке использования вместо Koin
- Не размещай зависимости фичи вне `FeatureNameModule.kt` без явной причины
- Не смешивай зависимости нескольких фич в одном Koin-модуле
- Не регистрируй `ViewModel` без `viewModelOf`
- Не регистрируй зависимости без понимания их жизненного цикла
- Не передавай зависимости между слоями в обход архитектурных границ
- Не связывай implementation без контракта, если компонент должен использоваться через интерфейс
- Не добавляй в модуль зависимости, которые не используются фичей
- Не используй `get()` для зависимостей, которые не зарегистрированы в Koin
- Не создавай циклические зависимости между компонентами
- Не размещай бизнес-логику в Koin-модуле
- Не используй Koin-модуль как место для инициализации сценариев, навигации или работы с данными
- Не создавай отдельный Koin-модуль только для `Router` — регистрируй его в `XModule.get()`
- Не забывай регистрировать `XModule.get()` нового модуля в `AppModule.get()` в `App.kt`
- Не игнорируй правила `viewmodel.md`, `usecase.md`, `repository.md` и `navigation.md` при регистрации зависимостей

## Examples
### ✅ Correct
```text
home/
  HomeModule.kt        — Router interface, RouterImpl, ProviderImpl, Koin module
  presentation/
    screen/
      list/
        management/
          HomeListKey.kt
          HomeListState.kt
        route/
          HomeListRoute.kt
        screen/
          HomeListScreen.kt
          HomeListViewModel.kt
  domain/
    GetHomeUseCase.kt
    HomeModel.kt
    HomeRepository.kt
  data/
    HomeRepositoryImpl.kt
```

```kotlin
interface HomeRouter {
    fun gotoHomeList()
}

object HomeModule {

    fun get() = module {
        viewModelOf(::HomeListViewModel)
        single<HomeRouter> { HomeRouterImpl(get()) }
        singleOf(::HomeProviderImpl) bind Router.Provider::class

        factoryOf(::GetHomeUseCase)

        single<HomeRepository> {
            HomeRepositoryImpl(api = get(), dao = get())
        }
    }

    private class HomeRouterImpl(private val router: Router) : HomeRouter {
        override fun gotoHomeList() {
            router.goTo(HomeListKey)
        }
    }

    private class HomeProviderImpl : Router.Provider {
        override fun invoke(): EntryProviderInstaller = {
            entry<HomeListKey> {
                val viewModel = koinViewModel<HomeListViewModel>()
                HomeListRoute(viewModel = viewModel)
            }
        }
    }
}
```

Подключение в `AppModule`:
```kotlin
private object AppModule {
    fun get(): List<Module> = listOf(
        rootModule,
        HomeModule.get()
    )
}
```

### ❌ Incorrect
```kotlin
val appModule = module {
    single {
        HomeViewModel(
            getHomeUseCase = GetHomeUseCase(
                homeRepository = HomeRepositoryImpl(
                    api = get(),
                    dao = get()
                )
            )
        )
    }
}
```

```kotlin
val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    factoryOf(::GetHomeUseCase)
    factoryOf(::GetProfileUseCase)
}
```
