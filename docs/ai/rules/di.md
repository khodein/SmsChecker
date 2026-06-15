# di.md

## Purpose
Это правило описывает, как должно быть устроено внедрение зависимостей через Koin: модуль фичи (`XModule`), application-level модули, состав регистраций и связь с архитектурными слоями.

## Scope
Где применяется:
- All
- Правило распространяется на объявление Koin-модулей в фичах и в `app`, на получение зависимостей в `ViewModel`, `Block`, `Repository`, `UseCase`, навигационных классах.

## Principles
- DI в проекте — только Koin. Никаких ручных `MyClass(MyDep())` для сценариев, которые должны идти через DI.
- Каждая фича имеет свой `object XModule { fun get(): Module }`. Модуль перечисляет все зависимости фичи и связывает интерфейсы с реализациями.
- `XModule` живёт в `impl/<X>Module.kt` на корне пакета `impl`. **Не** «private classes внутри object XModule» — это устаревший паттерн.
- Реализации (`XRouterImpl`, `XProviderImpl`, `XRepositoryImpl`, `XUseCaseImpl`, `XViewModel`, блоки, мапперы) лежат в отдельных файлах с модификатором `internal` (см. `structure.md`). `XModule` ссылается на них через method reference (`::ClassName`).
- `AppModule.get()` в `app` собирает все `XModule.get()` и application-level модули (`AppDatabaseModule`, `RouterModule`, `ResModule`, `MainModule`).
- Жизненный цикл регистрации соответствует роли компонента: `viewModelOf` для `ViewModel`, `factoryOf` для `Block`, `singleOf` для `Repository`/`UseCase`/`Mapper`/`Router`/`Router.Provider`.

## Структура

```text
feature-<name>/impl/src/main/java/<root.package>/feature/<name>/
  XModule.kt                                ← public object XModule { fun get(): Module }

app/src/main/java/<root.package>/
  AppModule.kt                              ← object AppModule { fun get(): List<Module> }
  db/AppDatabaseModule.kt                   ← object AppDatabaseModule { fun get(): Module }
  router/RouterModule.kt                    ← object RouterModule { fun get(): Module } — корневой RouterImpl
  main/MainModule.kt                        ← object MainModule { fun get(): Module } — root-ViewModel и зависимости главного экрана

framework/tools/src/main/java/<root.package>/framework/tools/
  ResModule.kt                              ← object ResModule { fun get(): Module } — ResProvider
```

## Rules

### XModule (фича)
- Создавай `XModule` как `public object XModule { fun get(): Module }` в `impl/<X>Module.kt` на корне пакета фичи.
- Внутри `get()` возвращай Koin `module { ... }` с регистрациями фичи.
- Группируй регистрации по слоям комментариями: `// data`, `// domain`, `// navigation`, `// presentation`, `// mappers`.
- Регистрируй компоненты:
  - `singleOf(::XRepositoryImpl) bind XRepository::class` — Repository.
  - `singleOf(::XUseCaseImpl) bind XUseCase::class` — каждый UseCase (или `factoryOf`, если жизненный цикл фактории).
  - `singleOf(::XDataMapper)` (и `XDbMapper`, `XApiMapper`) — мапперы data-слоя.
  - `singleOf(::XRouterImpl) bind XRouter::class` — навигация.
  - `singleOf(::XProviderImpl) bind Router.Provider::class` — entry-provider фичи.
  - `viewModelOf(::XScreenViewModel)` — каждый `ViewModel`.
  - `factoryOf(::XBlockBlock)` — каждый блок (factoryOf, потому что блоки создаются заново при каждом attach к ViewModel).
  - `singleOf(::XScreenMapper)` и `singleOf(::XBlockMapper)` — мапперы presentation-слоя.
  - `factoryOf(::XDelegateImpl) bind XDelegate::class` — delegate-контракты фичи, если они есть.
- Не объявляй `XModule` вне `impl/` — он принадлежит реализации фичи.
- Не дроби фичу на несколько Koin-модулей без причины.
- Не смешивай регистрации разных фич в одном модуле.

### AppModule (`app`)
- Создавай `AppModule` в `app/.../AppModule.kt` как `object AppModule { fun get(): List<Module> }`.
- В `get()` возвращай список всех Koin-модулей: application-level (`RouterModule`, `AppDatabaseModule`, `ResModule`, `MainModule`) + по одному `XModule.get()` на фичу.
- При добавлении новой фичи — добавляй её `XModule.get()` в этот список.
- Не размещай регистрации зависимостей фичи прямо в `AppModule`. Только композиция модулей.

### Application-level модули (`app` и `framework/tools`)
- `RouterModule` (`app/router/`) регистрирует `RouterImpl` как реализацию `Router` из `framework/router`.
- `AppDatabaseModule` (`app/db/`) создаёт `AppDatabase` через `Room.databaseBuilder` и выставляет каждый `XDao` через `single { get<AppDatabase>().xDao() }`. См. `db.md`.
- `ResModule` (`framework/tools`) регистрирует `ResProvider` (через `ResProviderImpl(androidContext())`).
- `MainModule` (`app/main/`) регистрирует root-ViewModel главного экрана и его зависимости.

### Получение зависимостей
- В `ViewModel` и `Block` подключай зависимости через конструктор; Koin сам резолвит их.
- В `Route`-Composable получай `ViewModel` через `koinViewModel<XScreenViewModel>()`.
- В корневом `entryProvider` (`MainActivity`) `Router.Provider`-ы фич подбираются автоматически через `getKoin().getAll<Router.Provider>().forEach { it.invoke().invoke(this) }`.
- В обычных классах не используй `getKoin().get<T>()` напрямую — пробрасывай через конструктор.

## Do
- Создавай отдельный `XModule` на каждую фичу.
- Регистрируй `Repository`, `UseCase`, `Mapper`, `Router`, `Router.Provider` через `singleOf(::Impl) bind Interface::class`.
- Регистрируй `ViewModel` через `viewModelOf(::XScreenViewModel)`.
- Регистрируй блоки через `factoryOf(::XBlockBlock)`.
- Группируй регистрации внутри модуля по слоям с комментариями `// presentation`, `// block`,
  `// domain`, `// data`, `// delegate`, `// navigation`.
- Связывай интерфейс с реализацией через `bind`: `singleOf(::XRepositoryImpl) bind XRepository::class`.
- Подключай `XModule.get()` нового модуля в `AppModule.get()` сразу после создания.
- Подключай `framework/tools/ResModule.get()` в `AppModule.get()` один раз.
- Регистрируй `Router.Provider` именно как `bind Router.Provider::class` — иначе `getAll<Router.Provider>()` в `MainActivity` не подберёт его.
- Следуй правилам `viewmodel.md`, `block.md`, `usecase.md`, `repository.md`, `navigation.md`, `db.md` при регистрации соответствующих компонентов.

## Don't
- Не пиши `private class XRouterImpl : XRouter { ... }` внутри `object XModule` — это устаревший паттерн. Реализации живут в отдельных файлах `impl/router/XRouterImpl.kt` с модификатором `internal`.
- Не размещай `XModule` вне `impl/` фичи.
- Не создавай зависимости вручную в точке использования (`MyClass(MyDep())`), если они должны идти через Koin.
- Не размещай зависимости фичи в `AppModule` напрямую.
- Не смешивай зависимости нескольких фич в одном Koin-модуле.
- Не регистрируй `ViewModel` через `single` или `factory` — используй `viewModelOf`.
- Не регистрируй блоки через `single` — используй `factoryOf` (блоки re-attached при пересоздании `ViewModel`).
- Не забывай `bind` для интерфейсов — без него Koin не свяжет implementation с interface.
- Не создавай отдельный Koin-модуль только для `Router` фичи — регистрируй его в общем `XModule.get()`.
- Не используй `getKoin().get<T>()` в обычных классах — это маскирует зависимости. Используй конструктор.
- Не забывай добавить `XModule.get()` нового модуля в `AppModule.get()`.

## Examples

### ✅ Correct — `XModule` фичи с одним экраном и тремя блоками

```kotlin
// feature-x/impl/.../XModule.kt
object XModule {

    fun get(): Module = module {
        // data
        singleOf(::XRepositoryImpl) bind XRepository::class
        singleOf(::XDataMapper)

        // domain
        singleOf(::GetXUseCaseImpl) bind GetXUseCase::class
        singleOf(::SaveXUseCaseImpl) bind SaveXUseCase::class
        singleOf(::ObserveXListUseCaseImpl) bind ObserveXListUseCase::class

        // navigation
        singleOf(::XRouterImpl) bind XRouter::class
        singleOf(::XProviderImpl) bind Router.Provider::class

        // presentation
        viewModelOf(::XListViewModel)
        viewModelOf(::XEditViewModel)
        factoryOf(::XToolbarBlock)
        factoryOf(::XContentBlock)
        factoryOf(::XBottomBarBlock)

        // mappers
        singleOf(::XListMapper)
        singleOf(::XEditMapper)
        singleOf(::XToolbarMapper)
        singleOf(::XContentMapper)
        singleOf(::XBottomBarMapper)
    }
}
```

### ✅ Correct — реальный пример `ListeningModule`

```kotlin
// feature-listening/impl/.../ListeningModule.kt
object ListeningModule {

    fun get() = module {
        // presentation
        viewModelOf(::ListeningListViewModel)
        singleOf(::ListeningListMapper)
        singleOf(::ListeningMapper)
        singleOf(::ListeningBottomBarMapper)
        singleOf(::ListeningToolbarMapper)
        singleOf(::ListeningConfigMapper)
        singleOf(::ListeningHistoryMapper)

        // block
        factoryOf(::ListeningBlock)
        factoryOf(::ListeningToolbarBlock)
        factoryOf(::ListeningBottomBarBlock)
        factoryOf(::ListeningConfigBlock)
        factoryOf(::ListeningHistoryBlock)

        // domain
        factoryOf(::StartListeningUseCase)
        factoryOf(::StopListeningUseCase)
        factoryOf(::GetListeningUseCase)

        // data
        singleOf(::ListeningRepositoryImpl) bind ListeningRepository::class

        // delegate
        factoryOf(::ListeningNotificationDelegate)
        factoryOf(::ListeningSendingDelegate)
        factoryOf(::ListeningSmtpFacade) bind ListeningSendingFacade::class

        // navigation
        singleOf(::ListeningRouterImpl) bind ListeningRouter::class
        singleOf(::ListeningProviderImpl) bind Router.Provider::class
    }
}
```

### ✅ Correct — `AppModule`

```kotlin
// app/.../AppModule.kt
object AppModule {

    fun get(): List<Module> = listOf(
        RouterModule.get(),
        AppDatabaseModule.get(),
        ResModule.get(),
        MainModule.get(),
        XModule.get(),
        YModule.get(),
        ZModule.get(),
    )
}
```

### ✅ Correct — `RouterModule` в `app`

```kotlin
// app/router/RouterModule.kt
object RouterModule {
    fun get(): Module = module {
        singleOf(::RouterImpl) bind Router::class
    }
}
```

### ✅ Correct — `App.kt`

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(AppModule.get())
        }
    }
}
```

### ❌ Incorrect

```kotlin
// 1. private classes inside object XModule (устаревший паттерн)
object XModule {
    fun get(): Module = module {
        singleOf(::XRouterImpl) bind XRouter::class
    }

    private class XRouterImpl(private val router: Router) : XRouter { ... }   // ❌ выноси в отдельный файл, делай internal
}

// 2. ViewModel через single
object XModule {
    fun get(): Module = module {
        single { XListViewModel(get(), get()) }       // ❌ используй viewModelOf
    }
}

// 3. Блок через single
object XModule {
    fun get(): Module = module {
        singleOf(::XContentBlock)                      // ❌ блоки — factoryOf
    }
}

// 4. Регистрация без bind
object XModule {
    fun get(): Module = module {
        singleOf(::XRepositoryImpl)                    // ❌ нет bind — Koin не свяжет XRepository
    }
}

// 5. Все фичи в одном модуле
val appModule = module {
    viewModelOf(::XViewModel)
    viewModelOf(::YViewModel)                          // ❌ зависимости разных фич в одном модуле
    factoryOf(::GetXUseCaseImpl)
    factoryOf(::GetYUseCaseImpl)
}

// 6. Ручное создание зависимостей вместо DI
internal class XContentBlock(
    private val getXUseCase: GetXUseCase = GetXUseCaseImpl(XRepositoryImpl(...)),   // ❌
) : Block<...>() { ... }

// 7. XModule вне impl
// feature-x/api/.../XModule.kt
object XModule { ... }                                  // ❌ Koin-модуль — это impl

// 8. Забыли подключить XModule в AppModule
object AppModule {
    fun get() = listOf(
        RouterModule.get(),
        // ❌ XModule.get() пропущен — Koin не найдёт зависимости фичи
    )
}

// 9. Прямое использование Koin вместо конструктора
internal class XContentBlock(...) : Block<...>() {
    private val useCase: GetXUseCase by inject()        // ❌ инжекти через конструктор
}
```
