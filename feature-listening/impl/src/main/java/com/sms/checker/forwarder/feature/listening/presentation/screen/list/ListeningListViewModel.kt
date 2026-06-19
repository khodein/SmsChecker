package com.sms.checker.forwarder.feature.listening.presentation.screen.list

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.ListeningBottomBarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.ListeningConfigBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.ListeningHistoryBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.ListeningBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.ListeningToolbarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.mapper.ListeningListMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListState
import com.sms.checker.forwarder.framework.BaseViewModel

internal class ListeningListViewModel(
    private val listeningListMapper: ListeningListMapper,
    private val listeningBlock: ListeningBlock,
    private val listeningBottomBarBlock: ListeningBottomBarBlock,
    private val listeningToolbarBlock: ListeningToolbarBlock,
    private val listeningConfigBlock: ListeningConfigBlock,
    private val listeningHistoryBlock: ListeningHistoryBlock,
) : BaseViewModel<ListeningListState, ListeningListAction>() {

    override val action: ListeningListAction = ListeningListAction(
        bottomBarAction = listeningBottomBarBlock.action,
        listeningAction = listeningBlock.action,
        toolbarAction = listeningToolbarBlock.action,
        historyAction = listeningHistoryBlock.action,
        configAction = listeningConfigBlock.action
    )

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(listeningToolbarBlock)
            add(listeningBlock)
            add(listeningBottomBarBlock)
            add(listeningConfigBlock)
            add(listeningHistoryBlock)
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
            listeningState = listeningBlock.blockState.value,
            listeningToolbarState = listeningToolbarBlock.blockState.value,
            listeningBottomBarState = listeningBottomBarBlock.blockState.value,
            listeningConfigState = listeningConfigBlock.blockState.value,
            listeningHistoryState = listeningHistoryBlock.blockState.value
        )
    }
}
