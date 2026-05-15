# screen.md

## Purpose
Это правило описывает, как должны быть устроены экран и UI-компоненты в слое presentation

## Scope
Где применяется:
- Presentation
- Правило распространяется на экраны, Composable-функции и структуру UI в слое presentation

## Principles
- Экран отвечает только за отображение состояния и пользовательские события
- UI не должен содержать бизнес-логику и прямую работу с данными
- Composable-функции должны работать через `UiState` и callbacks, а доступ к `ViewModel` должен оставаться во внешнем `Route`
- Экран должен оставаться простым по структуре и ответственности

## Rules
- Создавай экран только как часть слоя presentation
- Разделяй `Route`-Composable и `Screen`-Composable по их ответственности
- Используй `Composable`-функции только для отображения UI и обработки пользовательских событий
- Используй для экрана `UiState`, который наследуется от `BaseUiState`
- Получай данные для экрана только из `UiState`
- Передавай пользовательские действия из экрана только в `ViewModel`
- Не обращайся к `Repository`, API, базе данных и `UseCase` напрямую из экрана
- Не размещай бизнес-логику в экране и Composable-функциях
- Не выполняй запросы к API и базе данных из Composable-функций
- Не передавай `request`, `response`, `entity` и domain-модели напрямую в Composable-функции
- Используй только `UiState` и UI-модели, если они нужны для отображения
- Разделяй крупный экран на небольшие Composable-компоненты, если это улучшает читаемость
- Храни навигационные действия в соответствии с правилами `navigation.md`
- Храни логику состояния экрана в соответствии с правилами `viewmodel.md`
- Следуй структуре файлов из `docs/ai/rules/structure.md`

## Do
- Создавай внешний `Route`-Composable для работы с `ViewModel`
- Получай `UiState` во внешнем `Route`-Composable и передавай его в экран
- Создавай `UiState` экрана как часть framework-контракта на основе `BaseUiState`
- Создавай `Screen`-Composable как чистую UI-функцию, которая принимает `UiState`
- Передавай пользовательские действия из `Screen` наружу через callbacks
- Разделяй экран на небольшие Composable-компоненты, если это улучшает читаемость
- Выноси повторно используемые UI-части в отдельные Composable-функции
- Передавай в UI только данные, подготовленные для отображения
- Отображай загрузку, ошибку и успешное состояние через `UiState`
- Используй `MaterialTheme` для цветов, типографики и стандартных UI-стилей
- Следуй правилам `viewmodel.md` для работы с состоянием и `navigation.md` для навигации

## Don't
- Не используй для экрана `UiState`, который не наследуется от `BaseUiState`
- Не передавай `ViewModel` напрямую в `Screen`-Composable
- Не получай `UiState` внутри `Screen` через `ViewModel`
- Не подписывайся на `Flow`, `StateFlow` и другие источники состояния внутри `Screen`
- Не вызывай методы `ViewModel` напрямую из вложенных UI-компонентов, если можно передать callback
- Не передавай `Repository`, `UseCase`, API и другие зависимости в `Screen`
- Не выполняй навигацию напрямую из `Screen`, если она должна быть обработана на уровне `Route`
- Не размещай бизнес-логику в `Screen` и других Composable-функциях
- Не выполняй запросы к API и базе данных из `Screen`
- Не передавай `request`, `response`, `entity` и domain-модели напрямую в `Screen`
- Не перегружай `Screen` ответственностью за получение состояния, навигацию и бизнес-логику одновременно

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

`Route` получает состояние из `ViewModel` и передает в `Screen` только `HomeUiState` и callbacks.

```kotlin
@Composable
fun HomeRoute(
    viewModel: HomeViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    HomeScreen(
        state = uiState.value,
        onNumberClick = viewModel::onNumberClick,
        onRetryClick = viewModel::onRetryClick
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onNumberClick: (Int) -> Unit,
    onRetryClick: () -> Unit
) {
    when (state.status) {
        Status.LOADING -> {
            CircularProgressIndicator()
        }

        Status.ERROR -> {
            Button(onClick = onRetryClick) {
                Text(text = "Retry")
            }
        }

        Status.SUCCESS -> {
            LazyColumn {
                items(state.numbers) { number ->
                    Button(onClick = { onNumberClick(number) }) {
                        Text(text = number.toString())
                    }
                }
            }
        }
    }
}
```

### ❌ Incorrect
```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state = viewModel.uiState.collectAsState()

    LazyColumn {
        items(state.value.numbers) { number ->
            Button(onClick = { viewModel.onNumberClick(number) }) {
                Text(text = number.toString())
            }
        }
    }
}
```
