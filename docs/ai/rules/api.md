# api.md

## Purpose
Это правило описывает содержимое `api`-подмодуля feature-модуля: что именно фича выставляет наружу для других фич и `app`, а что обязано оставаться внутри `impl`.

## Scope
Где применяется:
- Каждый Gradle-субмодуль `feature-<name>/api/`.
- Правило распространяется на контракты, которые могут импортироваться из соседних feature-модулей и из `app`.

## Principles
- `api`-подмодуль — это **публичный контракт** фичи. Всё в `api` доступно другим фичам и `app` напрямую.
- `impl`-подмодуль — это реализация. Никто, кроме `app`, не должен подключать `impl` другой фичи.
- В `api` лежат только стабильные абстракции в терминах domain. Изменение в `api` — потенциальный breaking change для всех потребителей.
- `api` зависит только от `framework/tools`. Никаких Compose, Koin, Ktor, Coil, Room, Navigation 3, `framework`, `framework/router` — это всё подключается convention plugin'ом только в `impl`.
- `api` собран максимально лёгким: minimum dependencies, минимум классов, минимум публичной поверхности.

## Convention plugin

```kotlin
// feature-<name>/api/build.gradle.kts
plugins {
    id("<prefix>.android.feature.api")
}

android {
    namespace = "<root.package>.feature.<name>.api"
}
```

Plugin `<prefix>.android.feature.api`:
- применяет `<prefix>.android.library` и `<prefix>.android.core`;
- добавляет `implementation(project(":framework:tools"))`;
- не подключает Compose, Koin, Ktor, Coil, Room, Navigation 3, `framework`, `framework/router`.

Если фиче нужна доп. зависимость в `api` (редкий случай) — её добавляют точечно в `feature-<name>/api/build.gradle.kts`.

## Что лежит в `api`

```text
feature-<name>/api/src/main/java/<root.package>/feature/<name>/
  router/
    XRouter.kt                      ← public interface, методы goto<Screen>(...)
  domain/
    model/
      XModel.kt                     ← public data class (domain-модель)
      X<Detail>Model.kt             ← дополнительные domain-модели фичи
    usecase/
      GetXUseCase.kt                ← public interface, suspend operator fun invoke(...)
      SaveXUseCase.kt
      ObserveXUseCase.kt
      DeleteXUseCase.kt
    exception/
      XException.kt                 ← public sealed class XException : Exception()
```

| Что | Зачем |
|---|---|
| `XRouter` | другая фича или `app` инициирует переход на экран этой фичи |
| `XModel` | соседняя фича передаёт/принимает domain-данные через `UseCase` |
| `XUseCase` interface | соседняя фича/`app` исполняет бизнес-сценарий этой фичи |
| `XException` | соседняя фича/`app` ловит предсказуемые ошибки сценариев |

## Rules
- Размещай в `api` только:
  - `router/XRouter.kt` — публичный навигационный контракт фичи (см. `navigation.md`);
  - `domain/model/*Model.kt` — domain-модели, которые используются как параметры/результаты `UseCase`;
  - `domain/usecase/*UseCase.kt` — interface каждого `UseCase` (`suspend operator fun invoke(...)` или `Flow<T>`-возврат, см. `usecase.md`);
  - `domain/exception/XException.kt` — `sealed class XException : Exception()` с конкретными вариантами.
- Используй `public`-видимость по умолчанию (без модификаторов) во всех файлах `api`.
- Помечай domain-модели суффиксом `Model`.
- Имя `UseCase`-интерфейса начинается с глагола (`Get`, `Save`, `Update`, `Delete`, `Observe`, `Send`).
- Если фиче не нужен навигационный контракт (например, фича — чисто библиотечная), `XRouter` не создаётся.
- Если фиче не нужно выставлять domain-сценарии наружу, `usecase/` пуст.
- Если внутри `impl` нужны типы, которых нет в `api` (UI-модели блоков, конфиги пресентейшна, кэши) — они остаются в `impl`.
- Не добавляй классы в `api`, если их не использует хотя бы один внешний потребитель (другая фича или `app`). Лучше держать `api` маленьким.
- Не подключай в `feature-<name>/api/build.gradle.kts` Compose, Koin, Ktor, Coil, Room, Navigation 3 — этот код в `api` не нужен.
- Не завись в `api` от `:feature-<other>:impl`. Допустимо зависеть только от другой `:feature-<other>:api`, если cross-feature domain-модели должны быть совместимы (редкий случай — лучше дублировать domain-модель).

## Что НЕ лежит в `api`

| Класс | Где должен лежать | Почему |
|---|---|---|
| `XRepository` interface | `impl/domain/` | внутренний контракт фичи — снаружи не нужен |
| `XRepositoryImpl` | `impl/data/` | реализация скрыта |
| `XUseCaseImpl` | `impl/domain/usecase/` | реализация скрыта |
| `XDao`, `XEntity` | `impl/db/` | data-слой не выходит за границу фичи |
| `XRouterImpl`, `XProviderImpl` | `impl/router/` | реализация навигации скрыта |
| `XScreenKey : NavKey` | `impl/presentation/route/<screen>/` | ключ — деталь реализации навигации |
| `XViewModel`, `XState`, `XAction`, `XEvent` | `impl/presentation/screen/<screen>/` | presentation целиком в `impl` |
| `XScreen`, `XRoute`, `XBlock`, `XWidget`, `XMapper` | `impl/presentation/...` | presentation целиком в `impl` |
| `XDataMapper`, `XApiMapper`, `XDbMapper` | `impl/data/mapper/` | data-маппинг внутренний |
| `XModule` | `impl/<X>Module.kt` | Koin-модуль фичи живёт в `impl` |
| `Request`, `Response` модели | `impl/data/` | data-модели наружу не выходят |

## Внешние API

Если фича работает с внешним сетевым API (Ktor):

- api-клиент (`XApi`, `XApiClient`, `XService`) — `internal class` в `impl/data/api/`.
- `XRequest` / `XResponse` модели — `internal data class` в `impl/data/api/model/`.
- Маппинг `XResponse` → `XModel` выполняется внутри `XApiMapper` (см. `repository.md`).
- Ничего из `impl/data/api/` не выносится в `api`-подмодуль.

То есть слово «api» используется в проекте в двух смыслах:
- **api-подмодуль фичи** (`feature-<name>/api/`) — публичный контракт фичи для других модулей;
- **внешний api-клиент** (`impl/data/api/`) — работа с сетью, внутри `impl`.

Не смешивай эти понятия.

## Do
- Держи `api` маленьким — только то, что реально нужно снаружи.
- Описывай в `api` контракты в терминах domain, без упоминания DB, network и UI.
- Используй publlic-видимость без модификаторов во всех файлах `api`.
- Перед добавлением нового класса в `api` задавай вопрос: «использует ли его другая фича или `app`?» Если ответ «нет» — место класса в `impl`.
- Согласовывай изменения в `api` с потребителями (соседними фичами и `app`).
- Подключай `feature-<other>:api` другой фичи через `implementation(project(":feature-<other>:api"))` в `feature-<name>/impl/build.gradle.kts`, если нужны её контракты.

## Don't
- Не подключай `feature-<other>:impl` в зависимости (`implementation(project(":feature-<other>:impl"))`) — никогда.
- Не выноси `XRepository` interface в `api`.
- Не выноси `XUseCaseImpl` в `api`.
- Не выноси `XDao`, `XEntity`, `XScreenKey`, `XModule` в `api`.
- Не выноси Compose-функции, Koin-модули, Room-классы в `api`.
- Не подключай Compose/Koin/Ktor/Coil/Room/Navigation в `feature-<name>/api/build.gradle.kts`.
- Не размещай реализации (даже «маленькие хелперы») в `api`. Всё в `api` — это interface или `data class` / `sealed class`.
- Не дублируй модели между `api` нескольких фич — если есть пересечение, обсуди с пользователем, лучше ли вынести модель в общий модуль или продублировать.
- Не добавляй классы в `api` спекулятивно «на будущее» — добавляй только когда появился реальный потребитель.

## Examples

### ✅ Correct — типичный `api` фичи

```text
feature-x/api/
  build.gradle.kts
  src/main/AndroidManifest.xml
  src/main/java/<root.package>/feature/x/
    router/
      XRouter.kt
    domain/
      model/
        XModel.kt
      usecase/
        GetXUseCase.kt
        SaveXUseCase.kt
        ObserveXListUseCase.kt
      exception/
        XException.kt
```

```kotlin
// router/XRouter.kt
interface XRouter {
    fun gotoXList()
    fun gotoXDetail(id: Long)
    fun gotoXEdit(id: Long? = null)
}

// domain/model/XModel.kt
data class XModel(
    val id: Long? = null,
    val name: String,
)

// domain/usecase/GetXUseCase.kt
interface GetXUseCase {
    suspend operator fun invoke(id: Long): XModel
}

// domain/usecase/ObserveXListUseCase.kt
interface ObserveXListUseCase {
    operator fun invoke(): Flow<List<XModel>>
}

// domain/usecase/SaveXUseCase.kt
interface SaveXUseCase {
    /** Вернёт id сохранённой записи. */
    suspend operator fun invoke(model: XModel): Long
}

// domain/exception/XException.kt
sealed class XException : Exception() {
    data class NotFound(val id: Long) : XException()
    data class NotUpdated(val id: Long) : XException()
}
```

```kotlin
// feature-x/api/build.gradle.kts
plugins {
    id("<prefix>.android.feature.api")
}

android {
    namespace = "<root.package>.feature.x.api"
}
```

### ✅ Correct — `api` фичи без навигации

Если фича — чисто библиотечная (например, аналитика, кэш), `api` содержит только domain-контракты, без `router/`.

```text
feature-y/api/src/main/java/<root.package>/feature/y/
  domain/
    model/YEventModel.kt
    usecase/SendYEventUseCase.kt
```

### ✅ Correct — соседняя фича использует `api` другой

```kotlin
// feature-z/impl/build.gradle.kts
plugins {
    id("<prefix>.android.feature")
}

dependencies {
    implementation(project(":feature-z:api"))
    implementation(project(":feature-x:api"))     // ✅ только api соседней фичи
}
```

```kotlin
// feature-z/impl/.../presentation/screen/.../ZSomeBlock.kt
internal class ZSomeBlock(
    private val xRouter: XRouter,                  // ✅ контракт из feature-x/api
    private val saveXUseCase: SaveXUseCase,        // ✅ контракт из feature-x/api
) : BaseBlock<...>() { ... }
```

### ❌ Incorrect

```kotlin
// 1. Repository interface в api
// feature-x/api/.../domain/XRepository.kt
interface XRepository { ... }                      // ❌ репозиторий — внутренний контракт impl/domain

// 2. ViewModel в api
// feature-x/api/.../XViewModel.kt
class XListViewModel : ... { ... }                 // ❌ presentation в api запрещён

// 3. DAO в api
// feature-x/api/.../db/XDao.kt
@Dao interface XDao { ... }                        // ❌ db в api запрещён

// 4. Compose-зависимость в api
// feature-x/api/build.gradle.kts
plugins {
    id("<prefix>.android.feature.api")
    id("<prefix>.android.compose")                  // ❌ api не должен зависеть от Compose
}

// 5. Зависимость на impl другой фичи
// feature-z/impl/build.gradle.kts
dependencies {
    implementation(project(":feature-x:impl"))     // ❌ только api соседней фичи
}

// 6. Реализация UseCase в api
// feature-x/api/.../usecase/GetXUseCase.kt
class GetXUseCase(                                  // ❌ в api — только interface
    private val repository: XRepository,
) {
    suspend operator fun invoke(): XModel = ...
}

// 7. Спекулятивный класс «на будущее» в api
// feature-x/api/.../domain/model/XFutureModel.kt
data class XFutureModel(...)                       // ❌ нет потребителей — место в impl до появления реального запроса
```
