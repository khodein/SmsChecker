# usecase.md

## Purpose
Это правило описывает, как должны быть устроены `UseCase` в domain-слое: контракт в `api`, реализация в `impl`, ответственность и сигнатура.

## Scope
Где применяется:
- Domain
- Правило распространяется на `UseCase`, domain-модели и бизнес-логику внутри domain-слоя.

## Principles
- `UseCase` отвечает только за один бизнес-сценарий.
- `UseCase` написан на чистом Kotlin: без Android, Compose, framework-зависимостей.
- `UseCase` работает только с domain-моделями (`XModel`) и domain-контрактами (`XRepository`, другие `UseCase`).
- `UseCase` не обращается напрямую к `Dao`, API и data-моделям (`Entity`, `Request`, `Response`).
- Контракт `UseCase` выставляется наружу как `interface` в `api`, реализация скрыта в `impl`. Это позволяет соседним фичам и `ViewModel`-ям из `app` вызывать `UseCase` через DI, не зная о деталях реализации.

## Структура

```text
feature-<name>/
  api/src/main/java/<root.package>/feature/<name>/domain/
    model/XModel.kt                       ← public data class
    usecase/
      GetXUseCase.kt                      ← public interface
      SaveXUseCase.kt                     ← public interface
      ObserveXUseCase.kt                  ← public interface (Flow-based)
    exception/XException.kt               ← public sealed class
  impl/src/main/java/<root.package>/feature/<name>/domain/
    XRepository.kt                        ← internal interface
    usecase/
      GetXUseCaseImpl.kt                  ← internal class : GetXUseCase
      SaveXUseCaseImpl.kt                 ← internal class : SaveXUseCase
      ObserveXUseCaseImpl.kt              ← internal class : ObserveXUseCase
```

## Rules
- Размещай `XUseCase` interface в `api/domain/usecase/`.
- Размещай `XUseCaseImpl` в `impl/domain/usecase/`.
- Объявляй `XUseCase` как `interface` с одним методом `invoke(...)`. Метод помечается `suspend operator fun invoke(...)` или возвращает `Flow<T>`.
- Объявляй `XUseCaseImpl` как `internal class XUseCaseImpl(...) : XUseCase`.
- Делай имя `UseCase` глагольным: `Get<X>UseCase`, `Save<X>UseCase`, `Update<X>UseCase`, `Delete<X>UseCase`, `Observe<X>UseCase`, `Send<X>UseCase`.
- Реализация лежит рядом, имя — `<Глагол><X>UseCaseImpl`.
- Передавай зависимости в `XUseCaseImpl` только через конструктор.
- В качестве зависимостей используй только domain-контракты: `XRepository`, другие `XUseCase` interfaces.
- Возвращай из `UseCase` domain-модели (`XModel`) или результаты, пригодные для бизнес-логики, не data-модели.
- Используй `suspend` для одноразовых операций, `Flow` для observable.
- Не используй `Request`, `Response`, `Entity` модели в `UseCase`.
- Не обращайся к `Dao`, API и базе данных напрямую — только через `XRepository`.
- Не используй Android-, Compose- и framework-типы в `UseCase`.
- Не возвращай из `UseCase` `UiState`, UI-модели и Compose-типы.
- Не вызывай `UseCase` из UI напрямую — только из `ViewModel` или `Block`.
- Не объединяй несколько разных бизнес-сценариев в одном `UseCase`.
- Регистрируй `UseCase` в `XModule` через Koin DSL: `singleOf(::GetXUseCaseImpl) bind GetXUseCase::class` (или `factoryOf`, если жизненный цикл соответствует фактории).

## Do
- Создавай отдельный `XUseCase` interface на каждый бизнес-сценарий.
- Размещай `XUseCase` interface в `api`, чтобы другие фичи и `app` могли его использовать.
- Размещай `XUseCaseImpl` в `impl` как `internal class`.
- Используй `suspend operator fun invoke(...)` для одноразовых операций.
- Используй возврат `Flow<T>` для observable-сценариев (`Observe<X>UseCase`).
- Передавай `XRepository` и другие domain-контракты через конструктор.
- Возвращай domain-модели и результаты, пригодные для бизнес-логики.
- Бросай domain-исключения (`XException`) из `UseCase`, если бизнес-логика требует прерывания сценария.
- Объединяй данные нескольких `Repository` внутри `UseCase`, если этого требует сценарий.
- Регистрируй `XUseCaseImpl` в Koin-модуле через `singleOf(::XUseCaseImpl) bind XUseCase::class` или `factoryOf(::XUseCaseImpl) bind XUseCase::class`.
- Следуй правилам `repository.md` для получения данных и `viewmodel.md` / `block.md` для интеграции с presentation.

## Don't
- Не размещай `XUseCase` interface в `impl` — он должен быть видим через `api`.
- Не размещай `XUseCaseImpl` в `api` — реализация скрыта.
- Не создавай `UseCase` как конкретный `class` в `api` без `interface` (соседние фичи не смогут переиспользовать контракт).
- Не называй класс бизнес-логики без суффикса `UseCase`.
- Не используй имя `UseCase` без глагольного префикса (`Get`, `Set`, `Save`, `Update`, `Delete`, `Observe`, `Send`).
- Не объединяй несколько бизнес-сценариев в одном `UseCase`.
- Не размещай `UseCase` вне domain-слоя.
- Не обращайся к API, базе данных и `Dao` напрямую из `UseCase`.
- Не передавай `Request`, `Response`, `Entity` модели в `UseCase`.
- Не используй Android-, Compose- и framework-типы в `UseCase`.
- Не возвращай data-модели или модели, подготовленные специально для UI.
- Не передавай `ViewModel`, `UiState` и UI-модели в `UseCase`.
- Не размещай логику отображения и навигации в `UseCase`.
- Не создавай `UseCase` без `suspend operator fun invoke(...)` или возврата `Flow`.
- Не игнорируй `XRepository`, если сценарий требует доступа к данным.
- Не нарушай принцип одной ответственности `UseCase` ради сокращения количества классов.
- Не вызывай `UseCase` напрямую из UI / Composable-функций.

## Examples

### ✅ Correct — одноразовый UseCase

```kotlin
// api/domain/model/XModel.kt
data class XModel(
    val id: Long,
    val name: String,
)

// api/domain/usecase/GetXUseCase.kt
interface GetXUseCase {
    suspend operator fun invoke(id: Long): XModel
}

// impl/domain/XRepository.kt
internal interface XRepository {
    suspend fun getById(id: Long): XModel
}

// impl/domain/usecase/GetXUseCaseImpl.kt
internal class GetXUseCaseImpl(
    private val repository: XRepository,
) : GetXUseCase {
    override suspend fun invoke(id: Long): XModel = repository.getById(id)
}

// impl/XModule.kt (фрагмент)
object XModule {
    fun get() = module {
        singleOf(::GetXUseCaseImpl) bind GetXUseCase::class
        // ...
    }
}
```

### ✅ Correct — observable UseCase через Flow

```kotlin
// api/domain/usecase/ObserveXListUseCase.kt
interface ObserveXListUseCase {
    operator fun invoke(): Flow<List<XModel>>
}

// impl/domain/usecase/ObserveXListUseCaseImpl.kt
internal class ObserveXListUseCaseImpl(
    private val repository: XRepository,
) : ObserveXListUseCase {
    override fun invoke(): Flow<List<XModel>> = repository.observeAll()
}
```

### ✅ Correct — UseCase, который объединяет несколько источников

```kotlin
// api/domain/usecase/GetXOverviewUseCase.kt
interface GetXOverviewUseCase {
    suspend operator fun invoke(): XOverviewModel
}

// impl/domain/usecase/GetXOverviewUseCaseImpl.kt
internal class GetXOverviewUseCaseImpl(
    private val xRepository: XRepository,
    private val yRepository: YRepository,
) : GetXOverviewUseCase {
    override suspend fun invoke(): XOverviewModel {
        val x = xRepository.getCurrent()
        val y = yRepository.getSummary()
        return XOverviewModel(x = x, y = y)
    }
}
```

### ✅ Correct — UseCase бросает domain-исключение

```kotlin
// api/domain/exception/XException.kt
sealed class XException : Exception() {
    data class NotFound(val id: Long) : XException()
}

// impl/domain/usecase/GetXUseCaseImpl.kt
internal class GetXUseCaseImpl(
    private val repository: XRepository,
) : GetXUseCase {
    override suspend fun invoke(id: Long): XModel =
        repository.findById(id) ?: throw XException.NotFound(id)
}
```

### ❌ Incorrect

```kotlin
// 1. UseCase обращается к API напрямую
class GetXUseCase(
    private val xApi: XApi,                          // ❌ должно быть XRepository
) {
    suspend operator fun invoke(): XResponse =        // ❌ Response — data-модель
        xApi.getX()
}

// 2. Реализация UseCase в api
// api/domain/usecase/GetXUseCase.kt
class GetXUseCase(                                    // ❌ В api — только interface
    private val repository: XRepository,
) {
    suspend operator fun invoke(): XModel = repository.getX()
}

// 3. UseCase возвращает UI-модель
interface GetXUseCase {
    suspend operator fun invoke(): XListItemState     // ❌ presentation-модель в domain
}

// 4. Объединение разных сценариев в одном UseCase
interface XUseCase {
    suspend fun getX(id: Long): XModel
    suspend fun saveX(model: XModel): Long            // ❌ два сценария — два разных UseCase
    suspend fun deleteX(id: Long)
}

// 5. UseCase без префикса-глагола
interface XUseCase { ... }                            // ❌ нужно Get/Save/Delete/Observe...

// 6. UseCase вызывается из Composable
@Composable
fun XScreen(getXUseCase: GetXUseCase) {               // ❌ UseCase только во ViewModel/Block
    val x by produceState<XModel?>(null) { value = getXUseCase(1) }
}
```
