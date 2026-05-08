# viewmodel.md

## Purpose
Это правило описывает, как должна быть устроена `ViewModel` в слое presentation

## Scope
Где применяется:
- Presentation
- Правило распространяется на `ViewModel`, `UiState` и управление состоянием экрана в слое presentation

## Principles
- `ViewModel` отвечает за управление состоянием экрана и обработку пользовательских действий
- `UiState` является частью framework-контракта presentation-слоя
- Бизнес-логика во `ViewModel` должна оставаться чистой и не зависеть от Android, Compose и framework-типов вне базовых контрактов проекта
- `ViewModel` должна быть связующим звеном между UI и domain-слоем, не нарушая границы архитектуры

## Rules
- Создавай `ViewModel` только как часть слоя presentation
- Наследуй каждую `ViewModel` от `BaseViewModel`
- Используй для каждой `ViewModel` `UiState`, который наследуется от `BaseUiState`
- Помечай `UiState` и дополнительные UI-модели аннотацией `@Immutable`
- Храни состояние экрана только во `ViewModel`
- Используй только одну `ViewModel` для одного экрана
- Используй только один основной `UiState` для одного экрана
- Обновляй состояние экрана только через `UiState`
- Используй `status` из `BaseUiState` для отображения состояний `LOADING`, `ERROR` и `SUCCESS`
- Передавай данные в UI только через `UiState`
- Обрабатывай пользовательские действия во `ViewModel`
- Вызывай `UseCase` из `ViewModel` для выполнения бизнес-логики
- Не обращайся к `Repository`, API и базе данных напрямую из `ViewModel`
- Не используй `request`, `response` и `entity` модели во `ViewModel`
- Не передавай `ViewModel` в `Screen`-Composable
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов и инфраструктуры `ViewModel`
- Сохраняй `UiState` immutable

## Do
- Создавай `ViewModel` как единую точку управления состоянием экрана
- Создавай отдельный `UiState` для каждого экрана
- Наследуй `UiState` экрана от `BaseUiState`
- Помечай `UiState` и дополнительные UI-модели аннотацией `@Immutable`
- Используй `status` для отображения состояний `LOADING`, `ERROR` и `SUCCESS`
- Храни в `UiState` только данные, необходимые для отображения экрана
- Обновляй `UiState` последовательно и предсказуемо в ответ на действия пользователя или результат `UseCase`
- Обрабатывай пользовательские действия в методах `ViewModel`
- Вызывай `UseCase` из `ViewModel` для выполнения бизнес-логики
- Не передавай domain-модели в UI-слой и преобразовывай их во `ViewModel` в данные, удобные для отображения
- Создавай дополнительные UI-модели внутри `UiState`, если экрану нужны отдельные модели для отображения
- Используй для дополнительных UI-моделей нейминг, понятный в контексте текущего `UiState`
- Делай `UiState` и дополнительные UI-модели immutable
- Используй только `val` в `UiState` и дополнительных UI-моделях без `var`
- Передавай в `Screen` только `UiState` и callbacks
- Используй `Flow` и `StateFlow` для управления состоянием экрана
- Следуй правилам `screen.md` для отображения состояния и `usecase.md` для бизнес-логики

## Don't
- Не создавай `ViewModel`, которая не наследуется от `BaseViewModel`
- Не используй `UiState`, который не наследуется от `BaseUiState`
- Не используй `UiState` и дополнительные UI-модели без аннотации `@Immutable`
- Не используй больше одной `ViewModel` для одного экрана
- Не используй больше одного основного `UiState` для одного экрана
- Не храни состояние экрана вне `UiState`
- Не используй `var` в `UiState` и дополнительных UI-моделях
- Не делай `UiState` и дополнительные UI-модели mutable
- Не передавай domain-модели в UI-слой
- Не передавай `request`, `response` и `entity` модели во `ViewModel`
- Не передавай `Repository`, API и базу данных напрямую во `ViewModel`
- Не размещай бизнес-логику, связанную с данными, вне `UseCase` и `ViewModel`
- Не вызывай `Repository`, API и базу данных напрямую из `ViewModel`
- Не передавай `ViewModel` в `Screen`-Composable
- Не передавай во `Screen` данные, которые не входят в `UiState`
- Не создавай дополнительные UI-модели вне `UiState`
- Не используй Android-, Compose- и UI-типы в бизнес-логике внутри `ViewModel`, кроме базовых контрактов проекта и инфраструктуры `ViewModel`
- Не обновляй состояние экрана вне `ViewModel`
- Не игнорируй правила `screen.md` и `usecase.md`, если они относятся к реализации `ViewModel`

## Examples
### ✅ Correct
```text
home/
  presentation/
    HomeRoute.kt
    HomeScreen.kt
    HomeViewModel.kt
    HomeUiState.kt
```

```kotlin
@Immutable
data class HomeUiState(
    val numbers: List<NumberItem> = emptyList(),
    override val status: Status = Status.SUCCESS
) : BaseUiState {
    @Immutable
    data class NumberItem(
        val id: Int,
        val title: String
    )
}

class HomeViewModel(
    private val getHomeNumbersUseCase: GetHomeNumbersUseCase
) : BaseViewModel<HomeUiState>() {
    override val initialUiState: HomeUiState = HomeUiState()
    private var loadJob: Job? = null

    fun load() {
        setState { copy(status = Status.LOADING) }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            runCatching {
                getHomeNumbersUseCase()
            }.onSuccess { numbers ->
                setState {
                    copy(
                        numbers = numbers.map { model ->
                            HomeUiState.NumberItem(
                                id = model.id,
                                title = model.title
                            )
                        },
                        status = Status.SUCCESS
                    )
                }
            }.onFailure {
                setState {
                    copy(status = Status.ERROR)
                }
            }
        }
    }
}
```

### ❌ Incorrect
```kotlin
data class HomeUiState(
    var numbers: List<HomeModel> = emptyList(),
    override val status: Status = Status.SUCCESS
) : BaseUiState

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel()
```
