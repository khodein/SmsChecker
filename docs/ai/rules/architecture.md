# architecture.md

## Purpose
Это правило описывает архитектурное разделение ответственности в проекте: границы между слоями `presentation`, `domain`, `data`, а также правило публичности через `api`/`impl` внутри feature-модуля.

## Scope
Где применяется:
- All
- Правило распространяется на presentation, domain, data слои каждой фичи и на границы между `api` и `impl` субмодулями фичи.

## Principles
- Архитектура проекта строится на разделении ответственности между слоями presentation, domain и data.
- Каждый слой зависит только от своей зоны ответственности и не смешивает логику других слоёв.
- Бизнес-логика изолирована от UI, Android- и framework-зависимостей.
- Доступ к данным проходит через `UseCase` → `Repository` → источники данных, а не напрямую из UI.
- Внутри фичи дополнительно действует правило публичности: что фича выставляет наружу — лежит в `api`, что остаётся внутри — в `impl`.
- Соседние фичи могут зависеть только от `api` другой фичи, никогда от `impl`.

## Слои фичи

### Presentation (`impl/presentation/`)
- UI на Jetpack Compose: `Route`-Composable, `Screen`-Composable, `Widget`-Composable.
- `ViewModel`, `UiState`, `Action`, `Event`, `Block`-классы.
- `NavKey` экрана и `Router.Provider`-регистрация.
- Не содержит бизнес-логики — только сборку состояния и проксирование пользовательских действий в `UseCase` (через блоки/ViewModel).

### Domain
Domain слой разнесён по `api` и `impl`:

| Где | Что | Видимость |
|---|---|---|
| `api/domain/model/` | `XModel` — domain-модели | public |
| `api/domain/usecase/` | `XUseCase` — interface | public |
| `api/domain/exception/` | `XException` — sealed class | public |
| `impl/domain/` | `XRepository` — interface | internal |
| `impl/domain/usecase/` | `XUseCaseImpl` — реализация `XUseCase` | internal |

- `UseCase` — чистый Kotlin, одна бизнес-задача на класс.
- `UseCase` работает только с domain-моделями, не с `Entity`/`Request`/`Response`.
- `Repository` interface объявляет контракт data-слоя для domain.
- `Repository` interface **остаётся в `impl`**, потому что наружу он не нужен — соседние фичи и `app` обращаются к домену только через `UseCase` interfaces из `api`.

### Data (`impl/data/`, `impl/db/`)
- `XRepositoryImpl` — реализует `XRepository`.
- `data/mapper/XDataMapper` — преобразует `Entity` / `Request` / `Response` ↔ domain-`XModel`.
- `db/XDao` (interface) и `db/entity/XEntity` (Room entity).
- `data` слой использует Room (через `XDao`), Ktor (через api-классы) и т.п.
- Маппинг data-моделей в domain выполняется только внутри data-слоя.

## Rules
- Размещай UI-компоненты, `ViewModel`, `UiState`, `Action`, `Event`, `Block`, `NavKey`, `Router.Provider` только в `impl/presentation/`.
- Размещай domain-модели в `api/domain/model/` с суффиксом `Model` (`HomeModel`, `CartItemModel`).
- Размещай `UseCase` interfaces в `api/domain/usecase/`, реализации `UseCaseImpl` — в `impl/domain/usecase/`.
- Размещай domain-исключения в `api/domain/exception/` как `sealed class XException : Exception()`.
- Размещай `Repository` interface в `impl/domain/`, реализацию `RepositoryImpl` — в `impl/data/`.
- Размещай `Dao` и `Entity` в `impl/db/` (DAO как `@Dao interface`, Entity как `@Entity class`).
- Размещай `Request` / `Response` / `Entity` модели и data-мапперы только в `impl/data/` или `impl/db/`.
- Преобразовывай data-модели в domain-модели через мапперы внутри data-слоя.
- Не используй `Request` / `Response` / `Entity` модели за пределами data-слоя.
- Не передавай domain-модели напрямую в Composable-функции — они приходят в UI только как часть `UiState`.
- Не обращайся к слою data напрямую из presentation — только через `UseCase`.
- Не используй Android-, Compose- и framework-типы в domain-слое.
- Не размещай бизнес-логику в UI-компонентах и Composable-функциях.
- Выполняй запросы к API и базе данных только через `RepositoryImpl` в data-слое.
- Соседние фичи зависят только от `api` другой фичи; не подключай `impl` чужой фичи.
- Сохраняй границы между presentation, domain, data при добавлении новой функциональности.

## Do
- Создавай отдельные модели для `data`, `domain` и `presentation`, если у них разная ответственность.
- Для data-слоя используй явные суффиксы моделей: `Request`, `Response`, `Entity`.
- Для domain-слоя используй domain-модель с суффиксом `Model`, например `XModel`.
- Создавай мапперы для преобразования `Request`/`Response`/`Entity` в domain-`Model`.
- Возвращай из `Repository` domain-модели или типы, пригодные для бизнес-логики, а не модели источника данных.
- Передавай данные из `ViewModel` в UI только через `UiState`.
- Держи `UiState` immutable и отражай в нём только состояние экрана.
- Размещай бизнес-логику в `UseCase` и блоках; в `ViewModel` оставляй только сборку состояния и связку блоков.
- Используй `suspend`-функции и `Flow` в соответствии с ответственностью слоя.
- При добавлении новой функциональности сначала определяй, к какому слою и к какому подмодулю (`api` или `impl`) относится каждая новая сущность.
- Описывай детальные правила presentation-слоя в `viewmodel.md`, `block.md`, `screen.md`, `navigation.md`.
- Описывай детальные правила domain-слоя в `usecase.md`.
- Описывай детальные правила data-слоя в `repository.md`, `db.md`, `api.md` (для внешнего API).
- Описывай детальные правила внедрения зависимостей в `di.md`.
- Описывай детальное содержимое `api` подмодуля в `api.md`, `framework/*` — в `framework.md`.

## Don't
- Не используй одну и ту же модель одновременно в `data`, `domain` и `presentation`.
- Не называй domain-модели без суффикса `Model`.
- Не передавай `Request`, `Response`, `Entity` модели в `ViewModel`, `UiState` и Composable-функции.
- Не передавай domain-модели напрямую в Composable-функции.
- Не выполняй маппинг data-моделей в UI или Composable-функциях.
- Не размещай мапперы domain-моделей вне data-слоя.
- Не возвращай из `Repository` `Request`, `Response` или `Entity` модели.
- Не обращайся к API, базе данных или `Repository` напрямую из UI.
- Не размещай бизнес-логику в Composable-функциях.
- Не используй Android-, Compose- и framework-типы в domain-слое.
- Не смешивай ответственность `presentation`, `domain` и `data` в одном классе.
- Не размещай `Repository` interface в `api` — другим фичам он не нужен.
- Не размещай `UseCase` реализацию в `api` — наружу выставляется только интерфейс.
- Не подключай `impl` другой фичи в зависимости (`implementation(project(":feature-other:impl"))` — запрещено).
- Не обходи границы слоёв ради сокращения количества классов или упрощения реализации.
- Не игнорируй дополнительные `Don't` из `viewmodel.md`, `block.md`, `screen.md`, `navigation.md`, `usecase.md`, `repository.md`, `db.md`, `di.md`, `api.md`, `framework.md`.

## Examples

### Структура слоёв фичи `home`

```text
feature-home/
  api/
    domain/
      model/HomeModel.kt              ← domain-модель, public
      usecase/GetHomeUseCase.kt       ← UseCase interface, public
      exception/HomeException.kt      ← domain-исключения, public
    router/HomeRouter.kt              ← навигационный контракт, public
  impl/
    domain/
      HomeRepository.kt               ← Repository interface, internal
      usecase/GetHomeUseCaseImpl.kt   ← UseCase реализация, internal
    data/
      HomeRepositoryImpl.kt           ← Repository реализация, internal
      mapper/HomeDataMapper.kt        ← Entity ↔ HomeModel, internal
    db/
      HomeDao.kt                      ← @Dao interface, public
      entity/HomeEntity.kt            ← @Entity class, public
    presentation/
      ...                              ← Route/Screen/ViewModel/blocks, internal
    HomeModule.kt                     ← Koin module, public
    router/
      HomeRouterImpl.kt               ← internal
      HomeProviderImpl.kt             ← internal
```

### ✅ Correct — поток данных

```kotlin
// api/domain/model/HomeModel.kt
data class HomeModel(
    val id: Long,
    val title: String,
)

// api/domain/usecase/GetHomeUseCase.kt
interface GetHomeUseCase {
    suspend operator fun invoke(id: Long): HomeModel
}

// impl/domain/HomeRepository.kt
internal interface HomeRepository {
    suspend fun getHome(id: Long): HomeModel
}

// impl/domain/usecase/GetHomeUseCaseImpl.kt
internal class GetHomeUseCaseImpl(
    private val repository: HomeRepository,
) : GetHomeUseCase {
    override suspend fun invoke(id: Long): HomeModel = repository.getHome(id)
}

// impl/data/HomeRepositoryImpl.kt
internal class HomeRepositoryImpl(
    private val dao: HomeDao,
    private val mapper: HomeDataMapper,
) : HomeRepository {
    override suspend fun getHome(id: Long): HomeModel {
        val entity = dao.getById(id) ?: throw HomeException.NotFound(id)
        return mapper.toModel(entity)
    }
}
```

### ❌ Incorrect

```kotlin
// 1. Entity течёт в presentation
class HomeViewModel(
    private val dao: HomeDao,                       // ❌ ViewModel напрямую обращается к Dao
) : BaseViewModel<HomeState, HomeAction>(...) {
    fun load(id: Long) = viewModelScope.launch {
        val entity = dao.getById(id)                 // ❌ Entity в presentation
        setState { copy(entity = entity) }            // ❌ Entity в UiState
    }
}

// 2. Repository interface вынесен в api
// api/domain/HomeRepository.kt
interface HomeRepository { ... }                     // ❌ Repository interface должен быть в impl

// 3. UseCase Impl в api
// api/domain/usecase/GetHomeUseCase.kt
class GetHomeUseCase(private val repo: HomeRepository) {  // ❌ В api — только interface
    suspend operator fun invoke(): HomeModel = repo.getHome()
}

// 4. Подключение impl чужой фичи
dependencies {
    implementation(project(":feature-cart:impl"))    // ❌ только :feature-cart:api
}
```
