package com.sms.checker.forwarder.framework

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<UiState : BaseUiState> : ViewModel() {
    protected abstract val initialUiState: UiState

    private val _viewState: MutableState<UiState> = mutableStateOf(initialUiState)
    val viewState: State<UiState> = _viewState

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }
}
