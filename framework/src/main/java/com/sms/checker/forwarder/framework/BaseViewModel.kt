package com.sms.checker.forwarder.framework

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sms.checker.forwarder.framework.block.BlockStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState : BaseUiState>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val blockStore = BlockStore(
        scope = viewModelScope,
        savedStateHandle = savedStateHandle,
    )

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
            combine(flows) { }.collect {
                updateViewState()
            }
        }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected abstract fun updateViewState()

    abstract fun attach()
}
