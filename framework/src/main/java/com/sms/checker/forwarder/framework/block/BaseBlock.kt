package com.sms.checker.forwarder.framework.block

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import com.sms.checker.forwarder.framework.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseBlock<State : Any, Action, Provider> {

    private var onEvent: ((UiEvent) -> Unit)? = null
    protected var blockScope: CoroutineScope? = null
        private set

    abstract val action: Action

    protected var blockProvider: Provider? = null
        private set

    protected var savedStateHandle: SavedStateHandle? = null
        private set

    protected abstract fun getInitialUiState(): State

    private val _blockState by lazy { MutableStateFlow(getInitialUiState()) }
    val blockState: StateFlow<State>
        get() = _blockState.asStateFlow()

    fun attach(
        scope: CoroutineScope,
        savedStateHandle: SavedStateHandle,
        provider: Provider,
        onEvent: (UiEvent) -> Unit,
    ) {
        this.blockScope = scope
        this.savedStateHandle = savedStateHandle
        this.blockProvider = provider
        this.onEvent = onEvent
    }

    open fun start() {}

    protected fun setState(reducer: State.() -> State) {
        _blockState.value = blockState.value.reducer()
    }

    protected fun onEvent(event: UiEvent) {
        onEvent?.invoke(event)
    }

    protected abstract fun updateBlockState()
}
