# block.md

## Purpose
Это правило описывает, как должен быть устроен `Block` — самостоятельная единица логики и состояния отдельной части экрана.

## Scope
Где применяется:
- Presentation — внутри `impl/presentation/screen/<screen>/blocks/<block>/`.
- Правило распространяется на `Block`, его `State`, `Action`, опциональный `Event`, `Mapper` и `Widget`.

## Principles
- `Block` инкапсулирует логику и состояние отдельной части экрана (toolbar, content, bottombar, диалог, форма и т.п.).
- Каждый `Block` имеет собственные `State`, `Action`, опциональный `Event`, `Mapper` и `Widget`.
- `Block` взаимодействует с `UseCase` для бизнес-логики; никогда напрямую с `Repository`, API или базой.
- `ViewModel` только регистрирует блоки и собирает из них итоговый `UiState`.
- Если блоку нужны внешние callbacks (из `ViewModel` или соседних блоков), он объявляет nested `interface Provider`, который реализуется снаружи.

## Базовый контракт

```kotlin
abstract class Block<State : Any, Action, Provider> {

    abstract val action: Action

    protected val blockScope: CoroutineScope?
    protected val blockProvider: Provider?

    val blockState: StateFlow<State>

    protected abstract fun getInitialUiState(): State
    protected abstract fun updateBlockState()

    open fun start() {}

    protected fun setState(reducer: State.() -> State)
    protected fun onEvent(event: UiEvent)
}
```

- Три generic-параметра: `State` (тип состояния блока), `Action` (тип callbacks), `Provider` (тип внешних зависимостей или `Unit`, если их нет).
- `blockScope` — корутинный scope, привязанный к `ViewModel`; используется для асинхронных операций блока.
- `blockProvider` — объект, который снаружи реализует `Provider`-контракт блока; равен `Unit`, если блок не требует внешних зависимостей.
- `start()` — переопределяется, если блоку нужно запустить подписки или async-инициализацию после регистрации.

## Rules
- Создавай `Block` как `internal class` в `impl/presentation/screen/<screen>/blocks/<block>/`.
- Наследуй блок от `Block<XBlockState, XBlockAction, XBlockBlock.Provider>` (если есть `Provider`)
  или `Block<XBlockState, XBlockAction, Unit>` (если нет).
- Объявляй `interface Provider` вложенным в Block-класс (`XBlockBlock.Provider`), если блоку нужны внешние callbacks.
- Переопределяй `val action: XBlockAction` и инициализируй его ссылками на методы блока: `XBlockAction(onClickX = ::onClickX, onChangeY = ::onChangeY)`.
- Реализуй `getInitialUiState()` — возвращает начальное состояние блока (обычно через `XBlockMapper`).
- Реализуй `updateBlockState()` — обновляет состояние через `setState { copy(...) }` на основе текущих внутренних полей блока.
- Переопредели `start()`, если блок должен запустить подписки на `Flow` / `StateFlow` после регистрации.
- Используй `blockScope?.launch { }` для async-операций внутри блока.
- Используй `blockProvider` для доступа к внешним зависимостям, переданным при `add(block, provider)`.
- Размещай `XBlockState` и `XBlockAction` в подпапке `state/` внутри блока. Опциональный `XBlockEvent : UiEvent` — там же.
- Размещай `XBlockMapper` в подпапке `mapper/` внутри блока.
- Размещай `XBlockWidget` (Composable) в подпапке `widget/` внутри блока.
- Помечай `XBlockState`, `XBlockAction` и UI-модели аннотацией `@Immutable`. Используй только `val`.
- Регистрируй блок в `ViewModel` через `registerBlocks { add(block, provider) }` или `add(block)` для `Unit`-провайдера.
- Эмить one-time события через `onEvent(event)`; `ViewModel` пробросит их в общий `uiEvent` поток.
- Не обращайся к `Repository` и API напрямую из `Block` — используй `UseCase`.
- Не используй Android-, Compose- и UI-типы в логике `Block` (только в `Widget`).
- Не встраивай `Action` внутрь `State` — это два отдельных immutable объекта.

## Do
- Создавай отдельный `Block` для каждой логической части экрана.
- Структура блока: `XBlockBlock.kt` + `state/XBlockState.kt` + `state/XBlockAction.kt` (+ `state/XBlockEvent.kt` опц.) + `mapper/XBlockMapper.kt` + `widget/XBlockWidget.kt`.
- Инициализируй `action` в теле блока через `override val action = XBlockAction(...)` с method references на методы блока.
- Регистрируй блок в `ViewModel` через `registerBlocks { add(block) }` или `add(block, provider)`.
- Используй `blockScope?.launch { }` для async-операций внутри блока.
- Вызывай `updateBlockState()` после любого изменения внутреннего состояния.
- Используй `setState { copy(...) }` внутри `updateBlockState()`.
- Если блок зависит от внешних callbacks, объявляй `interface Provider` внутри блока и используй `blockProvider?.method()`.
- Передавай ресурсы (строки, иконки) в `XBlockState` через `XBlockMapper`, используя `ResProvider` из `framework/tools`.
- Подключай `UseCase`, `Mapper` и другие зависимости через конструктор блока и регистрируй их в `XModule` через `factoryOf(::XBlockBlock)`.

## Don't

- Не создавай `Block`, не наследующийся от `Block<State, Action, Provider>`.
- Не используй устаревшее имя базового класса `BaseBlock` — оно переименовано в `Block`.
- Не используй устаревшую сигнатуру `Block<State, Provider>` без параметра `Action`.
- Не размещай логику блоков напрямую во `ViewModel`.
- Не обращайся к `Repository` и API напрямую из `Block`.
- Не используй Android-, Compose- и UI-типы в логике `Block` (только в его `Widget`).
- Не делай `XBlockState`, `XBlockAction` и UI-модели mutable.
- Не используй `var` в `XBlockState` и `XBlockAction`.
- Не регистрируй один блок в нескольких `ViewModel`.
- Не встраивай `Action` в `State` как поле — они выставляются отдельно: `State` живёт в `UiState`, `Action` — в `XScreenAction`.
- Не передавай `Provider` через конструктор блока — его передаёт `ViewModel` через `registerBlocks { add(block, provider) }`.
- Не вызывай `start()` вручную — он вызывается автоматически после регистрации блока.

## Examples

### ✅ Correct — простой блок без Provider

```kotlin
@Immutable
internal data class XToolbarState(
    val title: String,
)

@Immutable
internal data class XToolbarAction(
    val onClickBack: () -> Unit,
)

internal class XToolbarMapper(
    private val resProvider: ResProvider,
) {
    fun map(): XToolbarState = XToolbarState(
        title = resProvider.getString(R.string.feature_x_toolbar_title),
    )
}

internal class XToolbarBlock(
    private val mapper: XToolbarMapper,
    private val xRouter: XRouter,
) : Block<XToolbarState, XToolbarAction, Unit>() {

    override val action = XToolbarAction(
        onClickBack = ::onClickBack,
    )

    override fun getInitialUiState(): XToolbarState = mapper.map()

    override fun updateBlockState() {
        // у блока нет изменяющегося внутреннего состояния
    }

    private fun onClickBack() {
        xRouter.goBack()
    }
}
```

### ✅ Correct — блок с async и без Provider

```kotlin
@Immutable
internal data class XContentState(
    val items: List<XItemModel>,
    val isLoading: Boolean,
)

@Immutable
internal data class XContentAction(
    val onClickItem: (Long) -> Unit,
)

internal class XContentBlock(
    private val getXListUseCase: GetXListUseCase,
    private val xRouter: XRouter,
) : Block<XContentState, XContentAction, Unit>() {

    private var items: List<XItemModel> = emptyList()
    private var isLoading: Boolean = true

    override val action = XContentAction(
        onClickItem = ::onClickItem,
    )

    override fun getInitialUiState(): XContentState =
        XContentState(items = items, isLoading = isLoading)

    override fun start() {
        blockScope?.launch {
            runCatching { getXListUseCase() }
                .onSuccess { result ->
                    items = result
                    isLoading = false
                    updateBlockState()
                }
                .onFailure {
                    isLoading = false
                    updateBlockState()
                }
        }
    }

    override fun updateBlockState() {
        setState { copy(items = items, isLoading = isLoading) }
    }

    private fun onClickItem(id: Long) {
        xRouter.gotoXDetail(id)
    }
}
```

### ✅ Correct — блок с Provider

`Provider` объявлен внутри блока. `ViewModel` реализует его и передаёт при регистрации.

```kotlin
@Immutable
internal data class XFormState(
    val name: String,
    val isValid: Boolean,
)

@Immutable
internal data class XFormAction(
    val onChangeName: (String) -> Unit,
    val onSubmit: () -> Unit,
)

internal class XFormBlock(
    private val mapper: XFormMapper,
) : Block<XFormState, XFormAction, XFormBlock.Provider>() {

    private var name: String = ""

    override val action = XFormAction(
        onChangeName = ::onChangeName,
        onSubmit = ::onSubmit,
    )

    override fun getInitialUiState(): XFormState = mapper.map(name)

    override fun updateBlockState() {
        setState { copy(name = name, isValid = name.isNotBlank()) }
    }

    private fun onChangeName(value: String) {
        name = value
        updateBlockState()
    }

    private fun onSubmit() {
        blockProvider?.onSubmit(XModel(name = name))
    }

    interface Provider {
        fun onSubmit(model: XModel)
    }
}
```

### ❌ Incorrect

```kotlin
// Устаревшая сигнатура без Action
internal class XToolbarBlock(...) : Block<XToolbarState, Unit>() { ... }   // ❌ нет Action

// Action как поле State
@Immutable
internal data class XToolbarState(
    val title: String,
    val action: XToolbarAction,                        // ❌ Action отдельно от State
)

// Прямое обращение к Repository
internal class XContentBlock(
    private val repository: XRepository,               // ❌ только через UseCase
) : Block<XContentState, XContentAction, Unit>() { ... }

// Compose-типы в логике блока
internal class XContentBlock(...) : Block<...>() {
    private var color: Color = Color.Red               // ❌ androidx.compose.ui.graphics.Color
    private fun onClick() {
        Modifier.fillMaxSize()                          // ❌ Compose в логике блока
    }
}

// Передача Provider через конструктор
internal class XFormBlock(
    private val provider: Provider,                    // ❌ Provider передаётся через add(block, provider)
) : Block<XFormState, XFormAction, XFormBlock.Provider>() { ... }
```
