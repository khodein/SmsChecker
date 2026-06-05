# db.md

## Purpose
Это правило описывает, как должна быть устроена работа с локальной базой данных Room: `XDao` и `XEntity` внутри фичи, application-level `AppDatabase` в `app/db/`, а также регистрация DAO через Koin.

## Scope
Где применяется:
- Data (`impl/db/` каждой фичи) и `app/db/`.
- Правило распространяется на `@Dao`-интерфейсы, `@Entity`-классы, `AppDatabase`, миграции и регистрацию DAO в Koin.

## Principles
- Application-level `RoomDatabase` (`AppDatabase`) — **один на весь проект** и живёт только в `app/db/`. Фичи не объявляют свои `RoomDatabase`.
- Каждая фича объявляет собственные `XDao` (интерфейс) и `XEntity` (Room entity) в `impl/db/`.
- `XDao` всегда `interface` — содержит только декларации Room-запросов, никакой бизнес-логики или ветвлений (`if`, `?:`, `when`).
- Логика, связанная с обработкой результатов DAO (`-1L`, `rowsAffected == 0`, нормализация nullable), живёт в `XRepositoryImpl`, не в DAO.
- `XDao` и `XEntity` — `public`, потому что их подключает `AppDatabase` в `app`.
- Все остальные классы внутри `impl/db/` (если такие появляются) — `internal`.
- Маппинг `XEntity` ↔ `XModel` выполняется в `XDataMapper` (см. `repository.md`).

## Структура

```text
feature-<name>/impl/src/main/java/<root.package>/feature/<name>/
  db/
    XDao.kt                        ← public @Dao interface
    entity/
      XEntity.kt                   ← public @Entity class

app/src/main/java/<root.package>/db/
  AppDatabase.kt                   ← @Database abstract class : RoomDatabase()
  AppDatabaseModule.kt             ← Koin module: создаёт Room и выставляет каждый XDao
  migration/                       ← опционально: ручные миграции
    AppMigration1To2.kt
    AppMigration2To3.kt
```

## Rules

### XDao (фича)
- Создавай `XDao` в `impl/db/XDao.kt`.
- Объявляй как `@Dao interface XDao` без модификатора видимости (`public`).
- Описывай только методы с Room-аннотациями: `@Insert`, `@Update`, `@Delete`, `@Query`, `@Upsert`, `@RawQuery`.
- Используй `suspend` для одноразовых операций (`@Insert`, `@Update`, `@Delete`, `@Query` для чтения).
- Используй возврат `Flow<T>` для observable-запросов (`@Query`).
- Возвращай `Long` из `@Insert` (id вставленной записи) или `List<Long>` для батча.
- Возвращай `Int` из `@Update` и `@Delete` (количество затронутых строк).
- Не размещай в DAO бизнес-логику, обработку nullable, `-1L`, `?: throw ...` — это задача `XRepositoryImpl`.
- Не размещай в DAO default-методы, `companion object` с константами SQL — выноси константы в `XEntity` или в отдельные `Sql<X>`-объекты, если нужны.
- Не объявляй DAO как `internal` — он должен быть видим из `app/db/AppDatabase.kt`.

### XEntity (фича)
- Создавай `XEntity` в `impl/db/entity/XEntity.kt`.
- Объявляй как `@Entity(tableName = "<feature>_<entity>_table") class XEntity(...)` без модификатора (`public`).
- Используй явное имя таблицы с префиксом фичи: `feature_email_smtp_table`, `feature_cart_item_table`.
- Помечай первичный ключ `@PrimaryKey`. Для автогенерируемых id — `@PrimaryKey(autoGenerate = true) val id: Long = 0`.
- Помечай каждое поле `@ColumnInfo(name = "<snake_case>")` с явным именем колонки.
- Для новых колонок, добавляемых после релиза, указывай `defaultValue` в `@ColumnInfo`, чтобы не ломать существующие записи.
- Не используй doman-модели как Room entity — entity и `XModel` всегда разные классы.
- Не объявляй `XEntity` как `internal` — он должен быть видим из `AppDatabase`.

### AppDatabase (app)
- Создавай `AppDatabase` в `app/db/AppDatabase.kt`.
- Объявляй как `@Database(entities = [...], version = N, exportSchema = false) abstract class AppDatabase : RoomDatabase()`.
- Перечисляй в `entities = [...]` все `XEntity` из всех фич.
- Объявляй по одному `abstract fun xDao(): XDao` на каждую фичу.
- При добавлении нового `XDao` / `XEntity` в фиче — обнови `AppDatabase` (entities + abstract fun) и подними `version`.

### AppDatabaseModule (app)
- Создавай `AppDatabaseModule` в `app/db/AppDatabaseModule.kt` как `object AppDatabaseModule { fun get(): Module = module { ... } }`.
- Создавай `AppDatabase` через `Room.databaseBuilder(...)` с `single { ... }` Koin.
- Имя БД задавай как `"<project>_db"` или другое стабильное.
- Выставляй каждый DAO через `single { get<AppDatabase>().xDao() }`.
- Подключай `AppDatabaseModule.get()` в `AppModule.get()`.
- Не размещай `AppDatabaseModule` в фиче.

### Миграции
- Для production-кода используй явные миграции `Migration(from, to) { db -> ... }` в `app/db/migration/`.
- `fallbackToDestructiveMigration()` допустим только на ранних стадиях разработки и должен быть удалён до релиза. Если он есть — явно отметить TODO.
- При добавлении новой колонки в `XEntity` создавай миграцию `AppMigration<N>To<N+1>` и регистрируй её в `Room.databaseBuilder`.
- Все миграции живут в `app/db/migration/` и не уходят в фичи.

### Подключение Room в feature-модуле
- Convention plugin `<prefix>.android.feature` подключает Room-зависимости автоматически.
- Если фича использует Room — никаких дополнительных шагов в `feature-<name>/impl/build.gradle.kts` не нужно.
- Для проектов без convention plugin: feature использует отдельный convention plugin `<prefix>.android.room` (см. `dependencies.md`).

## Do
- Создавай `XDao` как `@Dao interface XDao` с `suspend`/`Flow` методами.
- Создавай `XEntity` как `@Entity class XEntity` с явными `@ColumnInfo(name = "...")`.
- Префиксуй имя таблицы названием фичи: `feature_<name>_<entity>_table`.
- Используй `@PrimaryKey(autoGenerate = true) val id: Long = 0` для автогенерации id.
- Возвращай `Flow<List<XEntity>>` из `@Query` для observable-сценариев.
- Делай маппинг `XEntity ↔ XModel` в `XDataMapper` внутри `impl/data/mapper/`.
- Подключай `XDao` в `XRepositoryImpl` через конструктор.
- Регистрируй `XDao` в `AppDatabaseModule`: `single { get<AppDatabase>().xDao() }`.
- При добавлении новой фичи с БД — обновляй `entities = [...]` и `abstract fun xDao()` в `AppDatabase`, поднимай `version`, добавляй миграцию.
- Следуй правилам `repository.md` для интеграции DAO в data-слой.

## Don't
- Не объявляй `RoomDatabase` внутри фичи — он живёт только в `app/db/`.
- Не объявляй `XDao` как `internal` — он должен быть видим из `app/db/AppDatabase.kt`.
- Не объявляй `XEntity` как `internal` — он должен быть видим из `AppDatabase`.
- Не размещай бизнес-логику или нормализацию результатов в `XDao` (`?:`, `-1L`, `if/when`). Логика принадлежит `XRepositoryImpl`.
- Не используй `XEntity` как domain-модель и не возвращай его из `XRepository`.
- Не используй `XModel` как Room entity (`@Entity` поверх domain-модели запрещён).
- Не выноси `XDao` или `XEntity` в `api`-подмодуль фичи.
- Не оставляй `fallbackToDestructiveMigration()` в release-сборке без TODO/обсуждения с пользователем.
- Не создавай миграции внутри фичи.
- Не подключай Room-зависимости (`androidx.room:*`) прямой строкой в `feature-<name>/impl/build.gradle.kts` — это задача convention plugin.

## Examples

### ✅ Correct — DAO и Entity фичи

```kotlin
// feature-x/impl/.../db/entity/XEntity.kt
@Entity(tableName = "feature_x_table")
class XEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "is_active", defaultValue = "1")
    val isActive: Boolean,
)

// feature-x/impl/.../db/XDao.kt
@Dao
interface XDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: XEntity): Long

    @Update
    suspend fun update(entity: XEntity): Int

    @Query("SELECT * FROM feature_x_table WHERE id = :id")
    suspend fun getById(id: Long): XEntity?

    @Query("SELECT * FROM feature_x_table")
    suspend fun getAll(): List<XEntity>

    @Query("SELECT * FROM feature_x_table")
    fun observeAll(): Flow<List<XEntity>>

    @Query("DELETE FROM feature_x_table WHERE id = :id")
    suspend fun deleteById(id: Long)
}
```

### ✅ Correct — AppDatabase и Koin-модуль (app)

```kotlin
// app/.../db/AppDatabase.kt
@Database(
    entities = [
        XEntity::class,
        YEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun xDao(): XDao
    abstract fun yDao(): YDao
}

// app/.../db/AppDatabaseModule.kt
object AppDatabaseModule {
    fun get() = module {
        single {
            Room.databaseBuilder(
                context = androidContext(),
                klass = AppDatabase::class.java,
                name = "<project>_db",
            )
                .addMigrations(AppMigration1To2, AppMigration2To3)
                .build()
        }

        single { get<AppDatabase>().xDao() }
        single { get<AppDatabase>().yDao() }
    }
}
```

### ✅ Correct — миграция

```kotlin
// app/.../db/migration/AppMigration2To3.kt
val AppMigration2To3 = Migration(2, 3) { db ->
    db.execSQL(
        "ALTER TABLE feature_x_table ADD COLUMN is_active INTEGER NOT NULL DEFAULT 1"
    )
}
```

### ❌ Incorrect

```kotlin
// 1. RoomDatabase внутри фичи
// feature-x/impl/.../db/XDatabase.kt
@Database(entities = [XEntity::class], version = 1)
abstract class XDatabase : RoomDatabase()                // ❌ только в app/db/

// 2. DAO с бизнес-логикой / nullable обработкой
@Dao
interface XDao {
    @Query("SELECT * FROM feature_x_table WHERE id = :id")
    suspend fun getById(id: Long): XEntity?

    // ❌ default-метод с логикой — выноси в XRepositoryImpl
    suspend fun getByIdOrThrow(id: Long): XEntity =
        getById(id) ?: throw NoSuchElementException("X $id")
}

// 3. DAO как internal
@Dao
internal interface XDao { ... }                          // ❌ AppDatabase не увидит DAO

// 4. Entity как internal
@Entity(tableName = "...")
internal class XEntity(...)                              // ❌ AppDatabase не увидит Entity

// 5. Entity используется как domain-модель
@Entity(tableName = "...")
data class XModel(...)                                   // ❌ XModel — domain, XEntity — data. Разные классы

// 6. DAO/Entity в api
// api/db/XDao.kt
@Dao interface XDao { ... }                              // ❌ db-слой принадлежит impl

// 7. fallbackToDestructiveMigration без обоснования
single {
    Room.databaseBuilder(...)
        .fallbackToDestructiveMigration()                // ❌ для release нужны явные миграции
        .build()
}
```
