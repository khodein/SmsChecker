# screen.md

## Purpose
Это правило описывает, как должны быть устроены `Route`-Composable и `Screen`-Composable в слое presentation: их ответственности, передача состояния и `Action`, обработка one-time `UiEvent`.

## Scope
Где применяется:
- Presentation — `impl/presentation/route/<screen>/` и `impl/presentation/screen/<screen>/`.
- Правило распространяется на `Route`, `Screen`, `Widget`-композиции и подписку на `viewState` / `uiEvent`.

## Principles
- Экран разделён на два уровня:
  - `XScreenRoute` — связка `ViewModel` и UI. Подписывается на `viewState` и `uiEvent`, передаёт `state` и `action` в `XScreenScreen`.
  - `XScreenScreen` — чистый Composable. Принимает только `state` и `action`, не знает про `ViewModel` и Koin.
- UI не содержит бизнес-логики и прямой работы с данными.
- Все пользовательские действия идут через callbacks из `Action` — никаких прямых вызовов методов `ViewModel`.
- One-time события (snackbar, прокрутка, навигация) обрабатываются в `Route` через `LaunchedEffect`.

## Базовый шаблон

```kotlin
@Composable
internal fun XScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: XScreenViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    val snackbarHostState = AppTheme.snackBarHostState

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is XScreenEvent -> event.handle(snackbarHostState)
                is XBlockEvent -> event.handle(snackbarHostState)
            }
        }
    }

    XScreenScreen(
        modifier = modifier,
        state = state,
        action = action,
    )
}

@Composable
internal fun XScreenScreen(
    modifier: Modifier = Modifier,
    state: XScreenState,
    action: XScreenAction,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            XToolbarWidget(
                state = state.toolbarState,
                action = action.toolbarAction,
            )
        },
        bottomBar = {
            XBottomBarWidget(
                state = state.bottomBarState,
                action = action.bottomBarAction,
            )
        }
    ) { padding ->
        XContentWidget(
            modifier = Modifier.padding(padding),
            state = state.contentState,
            action = action.contentAction,
        )
    }
}
```

## Rules
- Создавай `XScreenRoute` в `impl/presentation/route/<screen>/XScreenRoute.kt`.
- Создавай `XScreenScreen` в `impl/presentation/screen/<screen>/XScreenScreen.kt`.
- Помечай обе функции `internal` и `@Composable`.
- Получай `ViewModel` только в `XScreenRoute` (через параметр; экземпляр приходит из `koinViewModel<XScreenViewModel>()` в `XProviderImpl`).
- Подписывайся на `viewModel.viewState` через `collectAsState()` только в `XScreenRoute`.
- Подписывайся на `viewModel.uiEvent` только в `XScreenRoute` через `LaunchedEffect(Unit) { viewModel.uiEvent.collect { ... } }`.
- Передавай в `XScreenScreen` только `state: XScreenState` и `action: XScreenAction` (плюс `modifier`).
- `XScreenScreen` композирует Widget-функции блоков, передавая каждому соответствующий `state.<X>State` и `action.<X>Action`.
- Используй `Scaffold`, `LazyColumn`, `Box` и стандартные Material3 контейнеры для верстки.
- Используй цвета, типографику и отступы из `AppTheme` / `MaterialTheme`.
- Не обращайся к `Repository`, API, базе данных и `UseCase` напрямую из `Route` или `Screen`.
- Не размещай бизнес-логику в Composable-функциях.
- Не выполняй API/DB-запросы из Composable-функций.
- Не передавай `Request` / `Response` / `Entity` или domain-модели в `Screen` — только UI-модели внутри `UiState`.
- Не подписывайся на `Flow` / `StateFlow` внутри `XScreenScreen` или Widget-функций.
- Не вызывай методы `ViewModel` напрямую из вложенных Composable — пробрасывай callback через `Action`.
- Не передавай `Router` в `Screen` — навигация инициируется блоками через `XRouter` или через подписку на `UiEvent` в `Route`.

## Do
- Разделяй `XScreenRoute` (связка с `ViewModel`) и `XScreenScreen` (чистый UI).
- Передавай `state` и `action` в `XScreenScreen` отдельными параметрами.
- Обрабатывай `UiEvent` в `XScreenRoute` через `LaunchedEffect`.
- Размещай обработку events в виде extension/local функций: `internal suspend fun XScreenEvent.handle(...)`.
- Используй `SnackbarHostState` из общей `AppTheme`-обёртки для показа snackbar.
- Делай `Widget`-функции блоков частью самих блоков (`impl/.../blocks/<block>/widget/XBlockWidget.kt`) и принимай в них `state: XBlockState` и `action: XBlockAction`.
- Если экрану нужны несколько `LazyListState`, `ScrollState` и т.п. — создавай их в `Route` через `remember` и пробрасывай вниз.
- Следуй правилам `viewmodel.md` для работы с состоянием, `block.md` для блоков и `navigation.md` для навигации.

## Don't
- Не используй `UiState`, который не наследуется от `UiState` (`framework`).
- Не передавай `ViewModel` напрямую в `XScreenScreen`.
- Не получай `UiState` внутри `XScreenScreen` через `ViewModel`.
- Не подписывайся на `Flow`, `StateFlow` и другие источники состояния внутри `XScreenScreen` или Widget-функций.
- Не вызывай методы `ViewModel` напрямую из вложенных UI-компонентов — пробрасывай callback через `Action`.
- Не передавай `Repository`, `UseCase`, API и другие зависимости в `Screen`.
- Не выполняй навигацию напрямую из `Screen` — навигация инициируется блоком через `XRouter` или через `UiEvent`.
- Не размещай бизнес-логику в `Screen` и других Composable-функциях.
- Не выполняй запросы к API и базе данных из `Screen`.
- Не передавай `Request`, `Response`, `Entity` и domain-модели напрямую в `Screen`.
- Не перегружай `Screen` ответственностью за получение состояния, навигацию и бизнес-логику одновременно.
- Не встраивай `Action` внутрь `UiState` как поле — `Screen` принимает `state` и `action` отдельными параметрами.

## Examples

### ✅ Correct

Полный пример экрана `XList`, у которого есть `toolbar` и `content` блоки, и one-time события — snackbar при ошибке.

```kotlin
// impl/presentation/screen/list/state/XListEvent.kt
@Immutable
internal data class XListEvent(
    val message: String,
    val isSuccess: Boolean,
) : UiEvent

internal suspend fun XListEvent.handle(snackbarHostState: SnackbarHostState) {
    snackbarHostState.showAppSnackBar(
        AppSnackBarVisuals(
            message = message,
            type = if (isSuccess) SnackBarType.Success else SnackBarType.Error,
        )
    )
}

// impl/presentation/route/list/XListRoute.kt
@Composable
internal fun XListRoute(
    modifier: Modifier = Modifier,
    viewModel: XListViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    val snackbarHostState = AppTheme.snackBarHostState

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is XListEvent -> event.handle(snackbarHostState)
            }
        }
    }

    XListScreen(
        modifier = modifier,
        state = state,
        action = action,
    )
}

// impl/presentation/screen/list/XListScreen.kt
@Composable
internal fun XListScreen(
    modifier: Modifier = Modifier,
    state: XListState,
    action: XListAction,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            XToolbarWidget(
                state = state.toolbarState,
                action = action.toolbarAction,
            )
        }
    ) { padding ->
        XContentWidget(
            modifier = Modifier.padding(padding),
            state = state.contentState,
            action = action.contentAction,
        )
    }
}
```

### ❌ Incorrect

```kotlin
// ViewModel внутри Screen
@Composable
fun XListScreen(
    viewModel: XListViewModel,                   // ❌ Screen не должен принимать ViewModel
) {
    val state by viewModel.viewState.collectAsState()
    LazyColumn {
        items(state.items) { item ->
            Button(onClick = { viewModel.onClick(item.id) }) {   // ❌ прямой вызов ViewModel
                Text(item.title)
            }
        }
    }
}

// Бизнес-логика и подписка в Composable
@Composable
fun XListScreen(state: XListState, action: XListAction) {
    val items by getXListUseCase().collectAsState(initial = emptyList())   // ❌ UseCase в Composable
    LaunchedEffect(Unit) {
        repository.refresh()                          // ❌ Repository в UI
    }
}

// Передача domain-модели в Screen
@Composable
fun XListScreen(items: List<XModel>) { ... }          // ❌ только UI-модели через UiState
```
