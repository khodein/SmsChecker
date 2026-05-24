# viewmodel.md

## Purpose
Это правило описывает, как должна быть устроена `ViewModel` в слое presentation

## Scope
Где применяется:
- Presentation
- Правило распространяется на `ViewModel`, `UiState` и управление состоянием экрана в слое presentation

## Principles
- `ViewModel` отвечает за сборку состояния экрана из блоков и связывание блоков между собой
- `UiState` является частью framework-контракта presentation-слоя
- Логика частей экрана инкапсулируется в отдельных `Block`-классах, а не в `ViewModel` напрямую
- `ViewModel` должна быть связующим звеном между блоками и экраном, не нарушая границы архитектуры

## Rules
- Создавай `ViewModel` только как часть слоя presentation
- Наследуй каждую `ViewModel` от `BaseViewModel<UiState>`
- Используй для каждой `ViewModel` `UiState`, который наследуется от `BaseUiState`
- Помечай `UiState` и дополнительные UI-модели аннотацией `@Immutable`
- Реализуй метод `attach()`, в котором вызывай `registerBlocks { }` для регистрации блоков
- Реализуй метод `updateViewState()`, который собирает `UiState` из состояний блоков
- Реализуй метод `getInitialUiState()`, который возвращает начальное состояние из блоков
- Регистрируй блоки через `registerBlocks { add(block, provider) }`
- Храни состояние экрана только во `ViewModel` через агрегацию состояний блоков
- Используй только одну `ViewModel` для одного экрана
- Используй только один основной `UiState` для одного экрана
- Обновляй состояние экрана только через `updateViewState()` или `setState { }`
- Используй `status` из `BaseUiState` для отображения состояний `LOADING`, `ERROR` и `SUCCESS`
- Передавай данные в UI только через `UiState`
- Не обращайся к `Repository`, API и базе данных напрямую из `ViewModel`
- Не используй `request`, `response` и `entity` модели во `ViewModel`
- Не передавай `ViewModel` в `Screen`-Composable
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов и инфраструктуры `ViewModel`
- Сохраняй `UiState` immutable

## Do
- Создавай `ViewModel` как точку сборки состояния экрана из блоков
- Создавай отдельный `UiState` для каждого экрана, наследуй от `BaseUiState`
- Помечай `UiState` и дополнительные UI-модели аннотацией `@Immutable`
- Используй `status` для отображения состояний `LOADING`, `ERROR` и `SUCCESS`
- Храни в `UiState` только данные, необходимые для отображения экрана
- Регистрируй блоки в `attach()` через `registerBlocks { add(block, provider) }`
- Собирай `UiState` из состояний блоков в `updateViewState()`
- Делай `UiState` и дополнительные UI-модели immutable
- Используй только `val` в `UiState` и дополнительных UI-моделях без `var`
- Передавай в `Screen` только `UiState`
- Используй `Flow` и `StateFlow` для управления состоянием экрана
- Следуй правилам `screen.md` для отображения состояния и `usecase.md` для бизнес-логики
- Следуй правилам `block.md` для реализации блоков

## Don't
- Не создавай `ViewModel`, которая не наследуется от `BaseViewModel<UiState>`
- Не используй `UiState`, который не наследуется от `BaseUiState`
- Не используй `UiState` и дополнительные UI-модели без аннотации `@Immutable`
- Не используй больше одной `ViewModel` для одного экрана
- Не используй больше одного основного `UiState` для одного экрана
- Не храни состояние экрана вне `UiState`
- Не используй `var` в `UiState`
- Не делай `UiState` mutable
- Не передавай domain-модели в UI-слой
- Не передавай `request`, `response` и `entity` модели во `ViewModel`
- Не передавай `Repository`, API и базу данных напрямую во `ViewModel`
- Не размещай логику частей экрана напрямую во `ViewModel` — выноси в блоки
- Не вызывай `Repository`, API и базу данных напрямую из `ViewModel`
- Не передавай `ViewModel` в `Screen`-Composable
- Не передавай во `Screen` данные, которые не входят в `UiState`
- Не создавай дополнительные UI-модели вне `UiState`
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов проекта и инфраструктуры `ViewModel`
- Не обновляй состояние экрана вне `ViewModel`
- Не игнорируй правила `screen.md`, `usecase.md` и `block.md`, если они относятся к реализации `ViewModel`

## Examples
### ✅ Correct
```kotlin
@Immutable
internal data class ListeningListState(
    override val status: Status,
    val listeningToolbarState: ListeningToolbarState,
    val listeningState: ListeningState,
    val listeningBottomBarState: ListeningBottomBarState,
    val items: List<ListeningListItemState> = emptyList(),
) : BaseUiState()

internal class ListeningListViewModel(
    private val listeningBlock: ListeningBlock,
    private val listeningBottomBarBlock: ListeningBottomBarBlock,
    private val listeningToolbarBlock: ListeningToolbarBlock,
) : BaseViewModel<ListeningListState>() {

    init {
        attach()
    }

    override fun updateViewState() {
        setState {
            copy(
                listeningState = listeningBlock.blockState.value,
                listeningToolbarState = listeningToolbarBlock.blockState.value,
                listeningBottomBarState = listeningBottomBarBlock.blockState.value
            )
        }
    }

    override fun attach() {
        registerBlocks {
            add(listeningToolbarBlock, Unit)
            add(listeningBlock, Unit)
            add(listeningBottomBarBlock, Unit)
        }
    }

    override fun getInitialUiState(): ListeningListState {
        return ListeningListState(
            status = Status.SUCCESS,
            listeningState = listeningBlock.blockState.value,
            listeningToolbarState = listeningToolbarBlock.blockState.value,
            listeningBottomBarState = listeningBottomBarBlock.blockState.value,
            items = emptyList(),
        )
    }
}
```

### ❌ Incorrect
```kotlin
// ViewModel напрямую содержит логику вместо блоков
class HomeListViewModel(
    private val getItemsUseCase: GetItemsUseCase
) : BaseViewModel<HomeListState>() {

    override fun attach() {
        // логика должна быть в блоке
        viewModelScope.launch { getItemsUseCase() }
    }
}

// var в UiState
data class HomeListState(
    var numbers: List<HomeModel> = emptyList()
) : BaseUiState()
```
