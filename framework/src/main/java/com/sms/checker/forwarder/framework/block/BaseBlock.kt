package com.sms.checker.forwarder.framework.block

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseBlock<State, Provider> {

    protected var blockScope: CoroutineScope? = null
        private set

    protected var blockProvider: Provider? = null
        private set

    protected var savedStateHandle: SavedStateHandle? = null
        private set

    protected abstract fun getInitialUiState(): State

    private val _blockState by lazy { MutableStateFlow(getInitialUiState()) }
    val blockState: StateFlow<State>
        get() = _blockState.asStateFlow()

    @CallSuper
    fun attach(
        scope: CoroutineScope,
        savedStateHandle: SavedStateHandle,
        provider: Provider,
    ) {
        this.blockScope = scope
        this.savedStateHandle = savedStateHandle
        this.blockProvider = provider
    }

    open fun start() {}

    protected fun setState(reducer: State.() -> State) {
        _blockState.value = blockState.value.reducer()
    }

    protected abstract fun updateBlockState()
}
