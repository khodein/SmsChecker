package com.sms.checker.forwarder.framework.block

import com.sms.checker.forwarder.framework.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class Block<State : Any, Action, Provider> {

    private var onEvent: ((UiEvent) -> Unit)? = null
    protected var blockScope: CoroutineScope? = null
        private set

    abstract val action: Action

    protected var blockProvider: Provider? = null
        private set

    protected abstract fun getInitialUiState(): State

    private val _blockState by lazy { MutableStateFlow(getInitialUiState()) }
    val blockState: StateFlow<State>
        get() = _blockState.asStateFlow()

    internal fun attach(
        scope: CoroutineScope,
        provider: Provider,
        onEvent: (UiEvent) -> Unit,
    ) {
        this.blockScope = scope
        this.blockProvider = provider
        this.onEvent = onEvent
    }

    /** Будет вызван один раз, и гарантирует что [blockScope] и [blockProvider] будут доступны*/
    open fun startBlock() {}

    protected fun setState(reducer: State.() -> State) {
        _blockState.value = blockState.value.reducer()
    }

    protected fun updateState(reducer: (State) -> State) {
        val state = blockState.value
        _blockState.value = reducer.invoke(state)
    }

    protected fun onEvent(event: UiEvent) {
        onEvent?.invoke(event)
    }

    protected abstract fun updateBlockState()

    open fun onUiStart() = Unit

    open fun onUiStop() = Unit
}
