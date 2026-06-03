package com.sms.checker.forwarder.framework

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sms.checker.forwarder.framework.block.BlockStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.collections.map

abstract class BaseViewModel<UiState : com.sms.checker.forwarder.framework.UiState, UiAction>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val blockStore = BlockStore(
        scope = viewModelScope,
        savedStateHandle = savedStateHandle,
        onEvent = ::onEvent
    )

    abstract val action: UiAction

    private val _viewState by lazy { MutableStateFlow(getInitialUiState()) }
    val viewState: StateFlow<UiState>
        get() = _viewState

    protected abstract fun getInitialUiState(): UiState

    protected fun registerBlocks(builder: BlockStore.() -> Unit) {
        if (blockStore.isRegister) return

        blockStore.apply(builder)
        val blocks = blockStore.build()
        if (blocks.isEmpty()) return

        viewModelScope.launch {
            val flows = blocks.map { it.blockState }
            combine(flows) { }.collectLatest {
                updateViewState()
            }
        }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected abstract fun updateViewState()

    protected fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    abstract fun attach()
}
