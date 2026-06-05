# repository.md

## Purpose
Это правило описывает, как должен быть устроен `Repository`: контракт в domain-слое, реализация в data-слое, маппинг data-моделей в domain-модели.

## Scope
Где применяется:
- Data (`impl/data/`, `impl/db/`) и Domain (`impl/domain/`).
- Правило распространяется на `XRepository`, `XRepositoryImpl`, `XDataMapper` и работу с источниками данных (Room, Ktor, DataStore, файловая система, внешние SDK).

## Principles
- `Repository` — единственная точка доступа к данным для domain-слоя.
- Контракт `XRepository` принадлежит domain-слою и описывается в его терминах: принимает и возвращает только domain-модели (`XModel`).
- Реализация `XRepositoryImpl` живёт в data-слое и знает про конкретные источники: `XDao`, Ktor-клиенты, `DataStore`, и т.п.
- `XRepository` interface **не выносится в `api`** — это внутренний контракт фичи между её domain и data. Соседние фичи и `app` работают с фичей только через `XUseCase` interfaces из `api`.
- `XRepositoryImpl` отвечает за маппинг data-моделей (`XEntity`, `XResponse`, `XRequest`) в domain-модели (`XModel`) через `XDataMapper`.

## Структура

```text
feature-<name>/impl/src/main/java/<root.package>/feature/<name>/
  domain/
    XRepository.kt                          ← internal interface
  data/
    XRepositoryImpl.kt                      ← internal class : XRepository
    mapper/
      XDataMapper.kt                        ← internal class
      (дополнительные мапперы по источникам, если нужно: XApiMapper, XDbMapper, XDataStoreMapper)
  db/
    XDao.kt                                 ← public @Dao interface (см. db.md)
    entity/
      XEntity.kt                            ← public @Entity class
```

Если фича работает с внешним API, файлы api-клиента (`XApi`, `XApiClient`) лежат в `impl/data/` или `impl/data/api/` (см. `api.md`).

## Rules
- Размещай `XRepository` в `impl/domain/XRepository.kt` как `internal interface`.
- Размещай `XRepositoryImpl` в `impl/data/XRepositoryImpl.kt` как `internal class XRepositoryImpl(...) : XRepository`.
- Возвращай из методов `XRepository` только domain-модели (`XModel`, `List<XModel>`, `Flow<List<XModel>>`) или примитивные типы (`Long`, `Boolean`).
- Используй `suspend`-функции для одноразовых операций и `Flow<T>` для observable.
- Размещай мапперы в `impl/data/mapper/` как `internal class`. Для разных источников можно создавать отдельные мапперы (`XDbMapper`, `XApiMapper`).
- Выполняй маппинг `Entity` / `Response` / `Request` ↔ `XModel` только внутри `XRepositoryImpl` или его мапперов.
- Подключай `XDao`, api-клиенты, `DataStore` и мапперы через конструктор `XRepositoryImpl`.
- Не возвращай из `XRepository` `Entity`, `Response`, `Request` или другие data-модели.
- Не пробрасывай `XDao` или api-клиент через границу `data` → `domain`.
- Не размещай бизнес-логику в `XRepository` — только агрегацию данных. Сложные сценарии живут в `UseCase`.
- Не размещай Android-, Compose- и framework-типы в `XRepository` и `XRepositoryImpl`.
- Бросай `XException` (из `api/domain/exception/`), если data-операция не выполнилась по предсказуемой причине (запись не найдена, конфликт, недоступность ресурса).
- Размещай инфраструктурные детали (`Dispatchers.IO`, обработка `withContext`) внутри `XRepositoryImpl`, а не пробрасывай их в `UseCase`.
- Если `XDao.update(entity)` возвращает количество затронутых строк или `Long` `-1L` при ошибке — нормализуй результат внутри `XRepositoryImpl` и бросай `XException`, если это бизнес-ошибка.
- Регистрируй `XRepositoryImpl` в `XModule` через `singleOf(::XRepositoryImpl) bind XRepository::class`.

## Do
- Создавай отдельный `XRepository` на фичу (или на доменную сущность фичи, если их несколько).
- Описывай методы `XRepository` в терминах бизнес-сценариев: `getById`, `getAll`, `observeAll`, `save`, `update`, `delete`, `send`.
- Возвращай domain-модели и `Flow<domain-модель>` из `XRepository`.
- Делай маппинг внутри data-слоя: `XDataMapper.toModel(entity)` / `toEntity(model)`.
- Используй `withContext(Dispatchers.IO)` для блокирующих операций (например, отправка email через JavaMail, файловые операции).
- Возвращай `Long`-id новой сущности из `save`-операций, если это нужно `UseCase`.
- Бросай `XException` для бизнес-ошибок (`NotFound`, `ConflictError`, `RemoteUnavailable`).
- Если фича хранит конфиг в `DataStore`, оборачивай работу с ним в `XRepositoryImpl`.
- Если фича работает с несколькими источниками (DB + API + DataStore), все они подключаются в `XRepositoryImpl` через конструктор.
- Регистрируй `XRepositoryImpl` в Koin: `singleOf(::XRepositoryImpl) bind XRepository::class`.
- Следуй правилам `db.md` для DAO/Entity, `api.md` для api-клиентов и `usecase.md` для использования `Repository` из domain.

## Don't
- Не размещай `XRepository` interface в `api`-подмодуле фичи.
- Не размещай `XRepository` interface в `data`-слое — он принадлежит domain.
- Не возвращай `Entity`, `Response`, `Request` модели из методов `XRepository`.
- Не выполняй маппинг data-моделей в UI или Composable-функциях.
- Не пробрасывай `XDao` или api-клиент через границу `data` → `domain`.
- Не используй `XRepository` напрямую из `ViewModel`, `Block`, `Screen` — только через `XUseCase`.
- Не размещай бизнес-логику в `XRepository` (валидации, сложные правила) — выноси в `UseCase`.
- Не размещай Android-, Compose- и framework-типы в `XRepository` и `XRepositoryImpl`.
- Не игнорируй `XException` — нормализуй результаты источников данных и бросай предсказуемые ошибки.
- Не делай `XRepositoryImpl` `public` — модификатор `internal`.

## Examples

### ✅ Correct — Repository с одним источником (DB)

```kotlin
// impl/domain/XRepository.kt
internal interface XRepository {
    suspend fun getById(id: Long): XModel
    suspend fun getAll(): List<XModel>
    fun observeAll(): Flow<List<XModel>>
    suspend fun save(model: XModel): Long
    suspend fun update(model: XModel): Long
    suspend fun deleteById(id: Long)
}

// impl/data/mapper/XDataMapper.kt
internal class XDataMapper {
    fun toModel(entity: XEntity): XModel = XModel(
        id = entity.id,
        name = entity.name,
    )

    fun toEntity(model: XModel): XEntity = XEntity(
        id = model.id ?: 0L,
        name = model.name,
    )
}

// impl/data/XRepositoryImpl.kt
internal class XRepositoryImpl(
    private val dao: XDao,
    private val mapper: XDataMapper,
) : XRepository {

    override suspend fun getById(id: Long): XModel {
        val entity = dao.getById(id) ?: throw XException.NotFound(id)
        return mapper.toModel(entity)
    }

    override suspend fun getAll(): List<XModel> =
        dao.getAll().map(mapper::toModel)

    override fun observeAll(): Flow<List<XModel>> =
        dao.observeAll().map { list -> list.map(mapper::toModel) }

    override suspend fun save(model: XModel): Long =
        dao.insert(mapper.toEntity(model))

    override suspend fun update(model: XModel): Long {
        val entity = mapper.toEntity(model)
        val rowsAffected = dao.update(entity)
        if (rowsAffected == 0) throw XException.NotUpdated(entity.id)
        return entity.id
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

// impl/XModule.kt (фрагмент)
object XModule {
    fun get() = module {
        singleOf(::XRepositoryImpl) bind XRepository::class
        singleOf(::XDataMapper)
    }
}
```

### ✅ Correct — Repository с двумя источниками (DB + remote API)

```kotlin
// impl/domain/XRepository.kt
internal interface XRepository {
    suspend fun refresh()
    fun observeAll(): Flow<List<XModel>>
}

// impl/data/XRepositoryImpl.kt
internal class XRepositoryImpl(
    private val dao: XDao,
    private val xApi: XApi,
    private val dbMapper: XDbMapper,
    private val apiMapper: XApiMapper,
) : XRepository {

    override suspend fun refresh() {
        val response = xApi.fetchAll()
        val entities = response.items.map(apiMapper::toEntity)
        dao.replaceAll(entities)
    }

    override fun observeAll(): Flow<List<XModel>> =
        dao.observeAll().map { list -> list.map(dbMapper::toModel) }
}
```

### ✅ Correct — Repository, выполняющий внешнюю операцию (отправка)

```kotlin
// impl/domain/XRepository.kt
internal interface XRepository {
    suspend fun send(message: String, config: XConfigModel)
}

// impl/data/XRepositoryImpl.kt
internal class XRepositoryImpl(
    private val xClient: XClient,
) : XRepository {

    override suspend fun send(message: String, config: XConfigModel) {
        withContext(Dispatchers.IO) {
            xClient.send(
                host = config.host,
                port = config.port,
                payload = message,
            )
        }
    }
}
```

### ❌ Incorrect

```kotlin
// 1. Repository interface вынесен в api
// api/domain/XRepository.kt
interface XRepository { ... }                          // ❌ контракт остаётся в impl/domain

// 2. Repository возвращает Entity
internal interface XRepository {
    suspend fun getById(id: Long): XEntity              // ❌ только domain-модели
}

// 3. Repository выполняет бизнес-валидацию
internal class XRepositoryImpl(...) : XRepository {
    override suspend fun save(model: XModel): Long {
        if (model.name.length < 3) throw IllegalArgumentException()   // ❌ бизнес-правила в UseCase
        return dao.insert(mapper.toEntity(model))
    }
}

// 4. Repository пробрасывает Dao наружу
internal interface XRepository {
    fun getDao(): XDao                                  // ❌ не выпускай Dao за границу data
}

// 5. ViewModel напрямую вызывает Repository
internal class XViewModel(
    private val repository: XRepository,                // ❌ только через UseCase
) : BaseViewModel<...>(...) { ... }

// 6. Android-типы в Repository
internal class XRepositoryImpl(
    private val context: Context,                       // ❌ Android-тип в data-логике; пробрасывай через ResProvider/DataStore-зависимости
) : XRepository { ... }

// 7. Repository публичный
class XRepositoryImpl(...) : XRepository { ... }        // ❌ должен быть internal
```
