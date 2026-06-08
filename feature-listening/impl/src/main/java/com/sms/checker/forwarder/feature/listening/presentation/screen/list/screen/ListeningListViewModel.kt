package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.ListeningBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.ListeningBottomBarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.ListeningConfigBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.ListeningToolbarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.mapper.ListeningListMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListAction
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status

internal class ListeningListViewModel(
    private val listeningListMapper: ListeningListMapper,
    private val listeningBlock: ListeningBlock,
    private val listeningBottomBarBlock: ListeningBottomBarBlock,
    private val listeningToolbarBlock: ListeningToolbarBlock,
    private val listeningConfigBlock: ListeningConfigBlock,
) : BaseViewModel<ListeningListState, ListeningListAction>(),
    ListeningConfigBlock.Provider,
    ListeningBlock.Provider {

    override val action: ListeningListAction = ListeningListAction(
        bottomBarAction = listeningBottomBarBlock.action,
        listeningAction = listeningBlock.action,
        toolbarAction = listeningToolbarBlock.action,
        configAction = listeningConfigBlock.action
    )

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(listeningToolbarBlock)
            add(listeningBlock, this@ListeningListViewModel)
            add(listeningBottomBarBlock)
            add(listeningConfigBlock, this@ListeningListViewModel)
        }
    }

    override fun updateViewState() {
        setState { buildState() }
    }

    override fun getInitialUiState(): ListeningListState {
        return buildState()
    }

    private fun buildState(): ListeningListState {
        return ListeningListState(
            status = Status.IDLE,
            listeningState = listeningBlock.blockState.value,
            listeningToolbarState = listeningToolbarBlock.blockState.value,
            listeningBottomBarState = listeningBottomBarBlock.blockState.value,
            listeningConfigState = listeningConfigBlock.blockState.value
        )
    }

    override fun isListening(): Boolean {
        return listeningBlock.isListeningState
    }

    override fun onConfigSizes(list: List<Boolean>) {
        listeningBottomBarBlock.updateConfigSizes(list)
    }

    override fun onUpdateListening(isListening: Boolean) {
        listeningBottomBarBlock.updateListening(isListening)
    }
}
