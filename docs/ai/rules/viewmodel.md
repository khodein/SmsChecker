# viewmodel.md

## Purpose
Это правило описывает, как должна быть устроена `ViewModel` в слое presentation: сборка состояния из блоков, выставление `Action`, работа с one-time `UiEvent`.

## Scope
Где применяется:
- Presentation
- Правило распространяется на `ViewModel`, `UiState`, `Action`, `UiEvent` и управление состоянием экрана.

## Principles
- `ViewModel` отвечает только за сборку состояния экрана из блоков и связывание блоков между собой.
- Бизнес-логика частей экрана инкапсулируется в `Block`-классах, а не пишется напрямую во `ViewModel`.
- `UiState` — единственный канал передачи состояния в UI.
- `Action` — отдельный объект с callbacks, выставляется как `val action` и передаётся в Composable отдельным параметром.
- One-time события (snackbar, навигация, прокрутка) идут через `UiEvent` + `SharedFlow`.
- Бизнес-логика во `ViewModel` и блоках написана на чистом Kotlin, без Android/Compose/framework-типов.

## Базовый контракт

`ViewModel` наследуется от `BaseViewModel<State, Action>`:

```kotlin
abstract class BaseViewModel<State : UiState, Action>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val uiEvent: SharedFlow<UiEvent>
    val viewState: StateFlow<State>

    abstract val action: Action

    protected abstract fun getInitialUiState(): State
    protected abstract fun updateViewState()
    abstract fun attach()

    protected fun registerBlocks(builder: BlockStore.() -> Unit)
    protected fun setState(reducer: State.() -> State)
    protected fun onEvent(event: UiEvent)
}
```

- `State` обязательно наследуется от `UiState` (из `framework`), а не от `BaseUiState`.
- `Action` — произвольный immutable класс со всеми callbacks экрана и его блоков.
- `SavedStateHandle` обязателен в конструкторе.

## Rules
- Создавай `ViewModel` только как часть слоя presentation (`impl/presentation/screen/<screen>/`).
- Наследуй каждую `ViewModel` от `BaseViewModel<XScreenState, XScreenAction>` с `SavedStateHandle` в конструкторе.
- Помечай `ViewModel` модификатором `internal`.
- Используй `UiState` (из `framework`) как базовый класс для `XScreenState`.
- Помечай `XScreenState`, `XScreenAction` и любые внутренние UI-модели аннотацией `@Immutable`.
- Реализуй `attach()` — внутри него вызывай `registerBlocks { }` для регистрации блоков. Вызывай `attach()` из `init { }`.
- Реализуй `getInitialUiState()` — возвращает начальное состояние, собранное из `blockState.value` блоков.
- Реализуй `updateViewState()` — пересобирает `UiState` из текущих `blockState.value` блоков; вызывается автоматически при изменении состояния любого зарегистрированного блока.
- Выставляй `val action: XScreenAction` как отдельное свойство; собирай его из `Action`-классов блоков и собственных callbacks `ViewModel`.
- Регистрируй блоки через `registerBlocks { add(block) }` (если блоку не нужен Provider) или `add(block, provider)` (если у блока объявлен `interface Provider`).
- Используй `setState { copy(...) }` для точечного обновления `UiState`; для полной пересборки — `updateViewState()` через `setState { buildState() }` или аналогично.
- Используй `status: Status` из `UiState` для отображения состояний `LOADING`, `ERROR`, `SUCCESS`, `IDLE`.
- Эмить one-time события через `onEvent(event: UiEvent)`. Подписка на `uiEvent` происходит в `Route`-Composable.
- Не обращайся к `Repository`, API и базе данных напрямую из `ViewModel` — только через `UseCase`.
- Не используй `Request`, `Response`, `Entity` модели во `ViewModel`.
- Не передавай `ViewModel` напрямую в `Screen`-Composable — Composable должен получать `state` и `action` отдельными параметрами.
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов `framework` (`BaseViewModel`, `SavedStateHandle`).
- Не размещай логику частей экрана напрямую во `ViewModel` — выноси в `Block`.

## Do
- Создавай `ViewModel` как точку сборки состояния экрана из блоков.
- Создавай отдельный `XScreenState` для каждого экрана, наследуй от `UiState`.
- Помечай `XScreenState`, `XScreenAction` и UI-модели аннотацией `@Immutable`.
- Используй `status` из `UiState` для индикации `LOADING` / `SUCCESS` / `ERROR` / `IDLE`.
- Храни в `XScreenState` только данные, необходимые для отображения экрана.
- Регистрируй блоки в `attach()` через `registerBlocks { add(block) }` или `add(block, provider)`.
- Собирай `UiState` из `blockState.value` блоков в `updateViewState()` (или в `buildState()`-хелпере).
- Собирай `XScreenAction` из `action`-полей блоков в `init` или в свойстве `action`.
- Передавай в `Screen` отдельные параметры `state: XScreenState` и `action: XScreenAction`.
- Используй `Flow` / `StateFlow` / `SharedFlow` для управления состоянием и событиями.
- Подключай `UseCase` через конструктор и Koin DSL `viewModelOf(::XScreenViewModel)`.
- Следуй правилам `screen.md` для рендера состояния, `block.md` для блоков, `usecase.md` для бизнес-логики.

## Don't
- Не создавай `ViewModel`, не наследующуюся от `BaseViewModel<State, Action>`.
- Не наследуй `UiState` от `BaseUiState` — такого класса в `framework` нет; базовый класс — `UiState`.
- Не используй сигнатуру `BaseViewModel<UiState>` или `BaseViewModel<State>` без `Action` — она устарела.
- Не забывай `SavedStateHandle` в конструкторе.
- Не используй `XScreenState` и UI-модели без `@Immutable`.
- Не используй больше одной `ViewModel` для одного экрана.
- Не используй больше одного основного `UiState` для одного экрана.
- Не храни состояние экрана вне `UiState`.
- Не используй `var` в `XScreenState` или `XScreenAction`.
- Не передавай domain-модели в UI-слой; для UI используй UI-модели внутри `UiState`.
- Не передавай `Request`, `Response`, `Entity` модели во `ViewModel`.
- Не подключай `Repository`, API и базу данных напрямую во `ViewModel`.
- Не размещай логику частей экрана напрямую во `ViewModel` — выноси в блоки.
- Не вызывай `Repository`, API и базу данных напрямую из `ViewModel`.
- Не передавай `ViewModel` в `Screen`-Composable.
- Не встраивай `Action` внутрь `UiState` как поле `action` — это устаревший паттерн. `Action` выставляется отдельно.
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов `framework`.

## Examples

### ✅ Correct — экран с двумя блоками без Provider

```kotlin
@Immutable
internal data class XListState(
    override val status: Status,
    val toolbarState: XToolbarState,
    val contentState: XContentState,
) : UiState()

@Immutable
internal data class XListAction(
    val toolbarAction: XToolbarAction,
    val contentAction: XContentAction,
)

internal class XListViewModel(
    private val xToolbarBlock: XToolbarBlock,
    private val xContentBlock: XContentBlock,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<XListState, XListAction>(savedStateHandle) {

    override val action = XListAction(
        toolbarAction = xToolbarBlock.action,
        contentAction = xContentBlock.action,
    )

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(xToolbarBlock)
            add(xContentBlock)
        }
    }

    override fun getInitialUiState(): XListState = buildState()

    override fun updateViewState() {
        setState { buildState() }
    }

    private fun buildState() = XListState(
        status = Status.SUCCESS,
        toolbarState = xToolbarBlock.blockState.value,
        contentState = xContentBlock.blockState.value,
    )
}
```

### ✅ Correct — экран с блоком, требующим Provider

`Provider` объявляется внутри блока; `ViewModel` реализует его и передаёт при регистрации.

```kotlin
internal class XEditViewModel(
    private val xFormBlock: XFormBlock,
    private val saveXUseCase: SaveXUseCase,
    private val mapper: XEditMapper,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<XEditState, XEditAction>(savedStateHandle),
    XFormBlock.Provider {

    override val action = XEditAction(
        formAction = xFormBlock.action,
    )

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(xFormBlock, this@XEditViewModel)
        }
    }

    override fun getInitialUiState(): XEditState = buildState(status = Status.IDLE)

    override fun updateViewState() {
        setState { buildState(status = status) }
    }

    private fun buildState(status: Status) = XEditState(
        status = status,
        formState = xFormBlock.blockState.value,
    )

    // XFormBlock.Provider
    override fun onSubmit(model: XModel) {
        viewModelScope.launch {
            setState { copy(status = Status.LOADING) }
            runCatching { saveXUseCase(model) }
                .onSuccess { onEvent(mapper.successEvent()) }
                .onFailure { onEvent(mapper.errorEvent(it)) }
            setState { copy(status = Status.SUCCESS) }
        }
    }

    private val status: Status get() = viewState.value.status
}
```

### ❌ Incorrect

```kotlin
// Бизнес-логика напрямую во ViewModel вместо блока
internal class XListViewModel(
    private val getXUseCase: GetXUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<XListState, XListAction>(savedStateHandle) {

    override fun attach() {
        viewModelScope.launch {                     // ❌ логика должна быть в блоке
            val items = getXUseCase()
            setState { copy(items = items) }
        }
    }
}

// Устаревшая сигнатура и UiState с одним параметром
internal class YViewModel : BaseViewModel<YState>() { ... }   // ❌ нет Action
internal data class YState(...) : BaseUiState()               // ❌ BaseUiState не существует

// Action внутри State
@Immutable
internal data class YState(
    override val status: Status,
    val action: YAction,                            // ❌ Action не должен быть полем State
) : UiState()

// ViewModel передан напрямую в Screen
@Composable
internal fun YScreen(viewModel: YViewModel) {       // ❌ Screen принимает state и action, не ViewModel
    val state by viewModel.viewState.collectAsState()
    // ...
}

// var в UiState
@Immutable
internal data class YState(
    var items: List<YItem> = emptyList(),           // ❌ только val
) : UiState()
```
