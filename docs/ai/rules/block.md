# block.md

## Purpose
Это правило описывает, как должен быть устроен `Block` — самостоятельная единица логики и состояния части экрана.

## Scope
Где применяется:
- Presentation — внутри `screen/{screenName}/screen/blocks/`
- Правило распространяется на `Block`, его `State`, `Action`, `Mapper` и `Widget`

## Principles
- `Block` инкапсулирует логику и состояние отдельной части экрана (toolbar, bottombar, список и т.д.)
- Каждый `Block` имеет собственный `State`, `Action`, `Mapper` и `Widget`
- `Block` взаимодействует с `UseCase`-ами для выполнения бизнес-логики
- `ViewModel` только регистрирует блоки и собирает из них итоговый `UiState`

## Rules
- Создавай `Block` как класс, наследующийся от `BaseBlock<State, Provider>`
- Реализуй `getInitialUiState()` — возвращает начальное состояние блока
- Реализуй `updateBlockState()` — обновляет состояние через `setState { copy(...) }`
- Переопредели `start()`, если блок должен запустить подписки или async-операции при инициализации
- Используй `blockScope` для запуска корутин внутри блока
- Используй `blockProvider` для доступа к внешним зависимостям, переданным при регистрации
- Создавай `Action`-класс внутри `state/` и встраивай его в `State` как поле `action`
- Инициализируй `action` в теле блока через ссылки на методы блока
- Передавай `action` в маппер при вызове `getInitialUiState()`
- Вызывай `updateBlockState()` при любом изменении внутреннего состояния
- Используй `setState { copy(...) }` для обновления `blockState`
- Помечай `State`, `Action` и вложенные UI-модели аннотацией `@Immutable`
- Используй только `val` в `State` и `Action` без `var`
- Не обращайся к `Repository` и API напрямую из `Block` — используй `UseCase`
- Не используй Android-, Compose- и UI-типы в логике `Block`

## Do
- Создавай отдельный `Block` для каждой логической части экрана
- Размещай `Block` в `blocks/{blockName}/` внутри папки экрана
- Создавай `State` и `Action` в `blocks/{blockName}/state/`
- Создавай `Mapper` в `blocks/{blockName}/mapper/`
- Создавай UI-компонент `Widget` в `blocks/{blockName}/widget/`
- Инициализируй `action` в теле блока: `private val action = XAction(onClick = ::onClick)`
- Регистрируй блок в `ViewModel` через `registerBlocks { add(block, provider) }`
- Используй `blockScope?.launch { }` для async-операций внутри блока
- Вызывай `updateBlockState()` после любого изменения внутреннего состояния блока

## Don't
- Не создавай `Block`, не наследующийся от `BaseBlock`
- Не размещай логику блоков напрямую во `ViewModel`
- Не обращайся к `Repository` и API напрямую из `Block`
- Не используй Android-, Compose- и UI-типы в логике `Block`
- Не делай `State`, `Action` и UI-модели mutable
- Не используй `var` в `State` и `Action`
- Не регистрируй один блок в нескольких `ViewModel`

## Examples
### ✅ Correct
```kotlin
internal class ListeningToolbarBlock(
    private val mapper: ListeningToolbarMapper,
) : BaseBlock<ListeningToolbarState, Unit>() {

    private val action = ListeningToolbarAction(
        onClickSettings = ::onClickSettings
    )

    override fun getInitialUiState(): ListeningToolbarState {
        return mapper.map(action)
    }

    override fun updateBlockState() {
        setState { copy(action = action) }
    }

    private fun onClickSettings() {
        // навигация или другая логика
    }
}

@Immutable
internal data class ListeningToolbarState(
    val title: String,
    val action: ListeningToolbarAction,
)

@Immutable
internal data class ListeningToolbarAction(
    val onClickSettings: () -> Unit,
)
```

### ✅ Correct (с async)
```kotlin
internal class ListeningBlock(
    private val mapper: ListeningMapper,
    private val getListeningObserveUseCase: GetListeningObserveUseCase,
    private val startListeningUseCase: StartListeningUseCase,
    private val stopListeningUseCase: StopListeningUseCase,
    getListeningUseCase: GetListeningUseCase,
) : BaseBlock<ListeningState, Unit>() {

    private var isListening: Boolean = getListeningUseCase.invoke()

    private val action = ListeningAction(
        onClickListening = ::onClickListening,
    )

    override fun getInitialUiState(): ListeningState {
        return mapper.map(isListening = isListening, action = action)
    }

    override fun start() {
        blockScope?.launch {
            getListeningObserveUseCase.invoke().collect {
                isListening = it
                updateBlockState()
            }
        }
    }

    override fun updateBlockState() {
        setState { copy(isListening = isListening) }
    }

    private fun onClickListening(value: Boolean) {
        isListening = value
        updateBlockState()
    }
}
```

### ❌ Incorrect
```kotlin
// Логика блока напрямую во ViewModel
class HomeViewModel(
    private val getItemsUseCase: GetItemsUseCase
) : BaseViewModel<HomeState>() {
    override fun attach() {
        viewModelScope.launch {
            getItemsUseCase().collect { /* ... */ }
        }
    }
}

// var в State
data class ToolbarState(
    var title: String
)
```
