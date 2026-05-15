package com.sms.checker.forwarder.framework

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<UiState : BaseUiState> : ViewModel() {
    protected abstract fun getInitialUiState(): UiState
    private val _viewState by lazy { MutableStateFlow(getInitialUiState()) }
    val viewState: StateFlow<UiState>
        get() = _viewState

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }
}
