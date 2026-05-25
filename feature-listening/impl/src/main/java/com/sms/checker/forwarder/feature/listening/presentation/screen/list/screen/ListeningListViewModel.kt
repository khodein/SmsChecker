package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen

import androidx.lifecycle.SavedStateHandle
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.ListeningBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.ListeningBottomBarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.ListeningConfigBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.ListeningToolbarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.mapper.ListeningListMapper
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status

internal class ListeningListViewModel(
    private val listeningListMapper: ListeningListMapper,
    private val listeningBlock: ListeningBlock,
    private val listeningBottomBarBlock: ListeningBottomBarBlock,
    private val listeningToolbarBlock: ListeningToolbarBlock,
    private val listeningConfigBlock: ListeningConfigBlock,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ListeningListState>(savedStateHandle) {

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(listeningToolbarBlock)
            add(listeningBlock)
            add(listeningBottomBarBlock)
            add(listeningConfigBlock)
        }
    }

    override fun updateViewState() {
        setState {
            copy(
                listeningState = listeningBlock.blockState.value,
                listeningToolbarState = listeningToolbarBlock.blockState.value,
                listeningBottomBarState = listeningBottomBarBlock.blockState.value,
                items = listeningListMapper.map(
                    configState = listeningConfigBlock.blockState.value
                )
            )
        }
    }

    override fun getInitialUiState(): ListeningListState {
        return ListeningListState(
            status = Status.SUCCESS,
            listeningState = listeningBlock.blockState.value,
            listeningToolbarState = listeningToolbarBlock.blockState.value,
            listeningBottomBarState = listeningBottomBarBlock.blockState.value,
            items = listeningListMapper.map(
                configState = listeningConfigBlock.blockState.value
            )
        )
    }
}
