# framework.md

## Purpose
Это правило описывает содержимое и назначение базовой инфраструктуры проекта — модулей `framework/`, `framework/router/`, `framework/tools/`. Эти модули задают контракты, которые используют все фичи.

## Scope
Где применяется:
- All
- Правило распространяется на содержимое трёх Gradle-модулей `framework`, `framework/router`, `framework/tools`, а также на правила использования их типов из фич.

## Principles
- `framework` — это **базовая инфраструктура проекта**, не место для фич. Сюда попадают только переиспользуемые контракты, базовые классы и общий UI-kit.
- `framework` разделён на три модуля, чтобы фичи могли выбирать минимальный набор зависимостей:
  - `framework/tools` — служебные утилиты (Kotlin/Android-окружение), без Compose и Koin-настройки. Подключается даже в `api`-подмодулях фич.
  - `framework/router` — навигационный контракт (требует Navigation 3, без Compose).
  - `framework/` — базовые контракты presentation, тема и UI-kit (требует Compose).
- Фичи зависят от framework-модулей через convention plugins, никогда напрямую `implementation(project(":framework"))` в фиче (вся проводка живёт в `build-logic/`).
- Содержимое `framework/*` не должно знать о существовании конкретных фич.

## Структура

```text
framework/                                          ← базовые контракты presentation + тема + UI-kit
  build.gradle.kts                                  ← plugin: <prefix>.android.library, .android.core, .android.compose
  src/main/java/<root.package>/framework/
    BaseViewModel.kt                                ← abstract class BaseViewModel<State : UiState, Action>
    UiState.kt                                      ← abstract class UiState(open val status: Status = Status.LOADING)
    UiEvent.kt                                      ← @Immutable interface UiEvent
    Status.kt                                       ← enum class Status { LOADING, ERROR, SUCCESS, IDLE }
    block/
      Block.kt                                      ← abstract class Block<State : Any, Action, Provider>
      BlockStore.kt                                 ← регистрация блоков, attach к scope/onEvent
      BlockProvider.kt                              ← marker interface
    theme/
      AppTheme.kt                                   ← entry-point темы и shared state (snackbarHostState и т.п.)
      Theme.kt
      Color.kt
      AppCorners.kt
      AppPaddings.kt
      AppTypography.kt
      AppLanguage.kt
    uikit/
      ButtonWidget.kt, IconButtonWidget.kt
      FieldWidget.kt
      CheckBoxWidget.kt
      TopAppBarWidget.kt, NavigationBackWidget.kt
      LoadingWidget.kt, ScreenLoadingWidget.kt
      SnackBarWidget.kt, AppSnackBarVisuals.kt

framework/router/                                   ← навигационный контракт
  build.gradle.kts                                  ← plugin: <prefix>.android.library, .android.navigation3
  src/main/java/<root.package>/framework/router/
    Router.kt                                       ← interface Router + interface Router.Provider + EntryProviderInstaller
    NavTransition.kt                                ← enum NavTransition + ключ метаданных

framework/tools/                                    ← служебные утилиты
  build.gradle.kts                                  ← plugin: <prefix>.android.library, .android.core, .koin
  src/main/java/<root.package>/framework/tools/
    ResModule.kt                                    ← object ResModule { fun get(): Module }
    res/ResProvider.kt                              ← interface ResProvider + internal ResProviderImpl
    time/
      LocalDateTimeFormatter.kt
      LocalDateTimePattern.kt
```

## Содержимое `framework`

### BaseViewModel

```kotlin
abstract class BaseViewModel<State : UiState, Action> : ViewModel() {

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

Детальные правила использования — в `viewmodel.md`.

### UiState

```kotlin
abstract class UiState(
    open val status: Status = Status.LOADING,
)
```

- Все экранные `XScreenState : UiState`.
- `status` используется UI для отображения состояний `LOADING`, `SUCCESS`, `ERROR`, `IDLE`.
- В проекте **нет** класса `BaseUiState` — это устаревшее имя, не используй.

### UiEvent

```kotlin
@Immutable
interface UiEvent
```

- Маркер one-time события (snackbar, навигация, scroll).
- Конкретные события объявляются как `internal data class XScreenEvent(...) : UiEvent` или `internal sealed class` в `state/` экрана/блока.
- Эмит — через `BaseViewModel.onEvent(event)` или `Block.onEvent(event)`.
- Подписка — в `XScreenRoute` через `viewModel.uiEvent.collect { ... }`.

### Status

```kotlin
enum class Status {
    LOADING,
    ERROR,
    SUCCESS,
    IDLE,
}
```

Используй `IDLE` для начального состояния без активности, `LOADING` — для процесса, `SUCCESS` — для готового состояния, `ERROR` — для ошибочного.

### Block

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

Детальные правила — в `block.md`.

### BlockStore

`BlockStore` — внутренняя инфраструктура `BaseViewModel`. Используется только через метод `registerBlocks { }`:

```kotlin
protected fun registerBlocks(builder: BlockStore.() -> Unit)
```

Внутри `builder` доступны методы:

- `fun <P> add(block: Block<*, *, P>, provider: P)` — регистрация блока с `Provider`.
- `fun add(block: Block<*, *, Unit>)` — регистрация блока без `Provider`.

После регистрации `BaseViewModel` подписывается на изменения `blockState` каждого блока и автоматически вызывает `updateViewState()`.

### Theme и UI-kit

- `AppTheme` — entry-point темы. Внутри пред оставляет `MaterialTheme`, общий `SnackbarHostState`, а также проектные `AppCorners`, `AppPaddings`, `AppTypography`.
- `uikit/*` — набор переиспользуемых Composable-компонентов (`ButtonWidget`, `FieldWidget`, `LoadingWidget`, `SnackBarWidget`, …).
- Никаких feature-специфичных компонентов в `framework/uikit/` быть не должно. Если компонент используется только одной фичей — он живёт в её `impl/.../widget/`.

## Содержимое `framework/router`

### Router

```kotlin
typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

interface Router {
    fun getBackStack(): List<NavKey>
    fun goTo(key: NavKey)
    fun goBack()

    interface Provider {
        operator fun invoke(): EntryProviderInstaller
    }
}
```

- Единственная реализация — `RouterImpl` в `app/router/`.
- Каждая фича реализует `Router.Provider` через `XProviderImpl` (см. `navigation.md`).

### NavTransition

```kotlin
const val NAV_TRANSITION_KEY = "nav_transition"

enum class NavTransition {
    SLIDE_HORIZONTAL,
    SLIDE_VERTICAL,
    FADE,
    NONE,
}

fun navTransitionMetadata(transition: NavTransition): Map<String, Any>
```

Используется в `entry<XKey>(metadata = navTransitionMetadata(NavTransition.SLIDE_VERTICAL)) { ... }` для управления анимацией перехода.

## Содержимое `framework/tools`

### ResProvider

```kotlin
interface ResProvider {
    fun getString(@StringRes id: Int): String
    fun getString(@StringRes id: Int, vararg args: Any): String
}
```

- Единственный способ получить строковый ресурс вне Composable.
- Используется в `Mapper`-классах блоков и экранов (`XBlockMapper`, `XScreenMapper`) для подстановки строк в `State`.
- Реализация (`ResProviderImpl`) — `internal`, скрыта внутри `framework/tools`.

### ResModule

```kotlin
object ResModule {
    fun get(): Module
}
```

- Регистрирует `ResProvider` в Koin.
- Подключается в `AppModule.get()` в `app`.

### Time utilities

`LocalDateTimeFormatter` и `LocalDateTimePattern` — обёртки над `java.time` для единого форматирования дат и времени. Используй их вместо ручного `DateTimeFormatter.ofPattern(...)`.

## Rules
- Не размещай feature-логику в `framework/*`.
- Не добавляй классы в `framework/uikit/`, если они нужны только одной фиче.
- Не дублируй контракты `framework` внутри фич (`BaseViewModel`, `UiState`, `UiEvent`, `Status`,
  `Block`).
- Используй `UiState` (не `BaseUiState`) как базовый класс для `XScreenState`.
- Используй `Router` и `Router.Provider` только из `framework/router`, не дублируй их.
- Используй `ResProvider` для строк в мапперах; не вызывай `Context.getString(...)` напрямую в presentation.
- Подключай `framework/*` через convention plugins (`<prefix>.android.feature`, `<prefix>.android.feature.api`). Не пиши `implementation(project(":framework"))` в `feature-<name>/*` руками — это уже сделано в convention plugin.
- При добавлении нового базового класса в `framework` сначала проверь, что он используется хотя бы в двух фичах. Если только в одной — оставь в фиче.

## Do
- Расширяй `framework/uikit/` теми компонентами, которые реально переиспользуются между фичами.
- Расширяй `framework/tools/` утилитами без UI-зависимостей (форматтеры, парсеры, обёртки над Android).
- Добавляй новые элементы `framework/router/`, только если они нужны механизму навигации в целом (новые типы переходов, типы entry).
- Согласовывай добавление новых базовых контрактов в `framework/` (`BaseViewModel`, `UiState`,
  `UiEvent`, `Block`, `BlockStore`) — это меняет всю архитектуру.
- Подключай `framework` к фиче только через convention plugins.

## Don't
- Не подключай Compose к `framework/router` и `framework/tools` — они работают без Compose.
- Не подключай Koin к `framework/` — DI поднимается только в `framework/tools/ResModule` и фичах.
- Не размещай в `framework/` зависимости от feature-модулей.
- Не выноси feature-specific Composable-функции (кастомные карточки, экраны) в `framework/uikit/`.
- Не дублируй `BaseViewModel`, `UiState`, `UiEvent`, `Block` внутри фич.
- Не используй устаревшие имена `BaseUiState`, `BaseBlock`, `BaseViewModel<State>` без `Action`,
  `Block<State, Provider>` без `Action` — они не существуют.
- Не подключай `framework/*` напрямую в `feature-<name>/*/build.gradle.kts` — это работа convention plugin.

## Examples

### ✅ Correct — `BaseViewModel` использует `UiState` и `UiEvent`

```kotlin
@Immutable
internal data class XListState(
    override val status: Status,
    val items: List<XItemState>,
) : UiState()

@Immutable
internal data class XListEvent(
    val message: String,
    val isSuccess: Boolean,
) : UiEvent

internal class XListViewModel(
    private val getXListUseCase: GetXListUseCase,
    private val mapper: XListMapper,
) : BaseViewModel<XListState, XListAction>() {

    override val action = XListAction(onClickItem = ::onClickItem)

    init { attach() }
    override fun attach() { registerBlocks { /* ... */ } }
    override fun getInitialUiState() = XListState(status = Status.IDLE, items = emptyList())
    override fun updateViewState() { /* ... */ }

    private fun onClickItem(id: Long) {
        viewModelScope.launch {
            runCatching { getXListUseCase() }
                .onFailure { onEvent(mapper.toErrorEvent(it)) }
        }
    }
}
```

### ✅ Correct — `Mapper` использует `ResProvider`

```kotlin
internal class XListMapper(
    private val resProvider: ResProvider,
) {
    fun toToolbarTitle(): String =
        resProvider.getString(R.string.feature_x_list_title)

    fun toErrorEvent(throwable: Throwable): XListEvent = XListEvent(
        message = resProvider.getString(R.string.feature_x_list_error),
        isSuccess = false,
    )
}
```

### ❌ Incorrect

```kotlin
// 1. Feature-specific компонент в framework/uikit
// framework/uikit/XListItemWidget.kt
@Composable fun XListItemWidget(...) { ... }            // ❌ widget принадлежит конкретной фиче

// 2. Использование устаревшего BaseUiState
internal data class XScreenState(...) : BaseUiState()    // ❌ класса BaseUiState нет, наследуй от UiState

// 3. Прямой Context.getString в мапперах
internal class XListMapper(private val context: Context) {  // ❌ используй ResProvider
    fun title(): String = context.getString(R.string.feature_x_list_title)
}

// 4. Подключение framework руками в feature
// feature-x/impl/build.gradle.kts
dependencies {
    implementation(project(":framework"))                // ❌ уже подключено convention plugin'ом
    implementation(project(":framework:router"))         // ❌
    implementation(project(":framework:tools"))          // ❌
}

// 5. RouterImpl внутри фичи
// feature-x/impl/router/RouterImpl.kt
class RouterImpl : Router { ... }                        // ❌ RouterImpl живёт только в app/router/
```
