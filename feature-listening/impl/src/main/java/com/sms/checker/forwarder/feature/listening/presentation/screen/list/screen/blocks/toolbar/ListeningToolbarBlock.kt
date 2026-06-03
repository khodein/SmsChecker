package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.mapper.ListeningToolbarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningToolbarBlock(
    private val listeningToolbarMapper: ListeningToolbarMapper,
): BaseBlock<ListeningToolbarState, ListeningToolbarAction, Unit>() {

    override val action = ListeningToolbarAction(
        onClickSettings = ::onClickSettings
    )

    override fun getInitialUiState(): ListeningToolbarState {
        return listeningToolbarMapper.map()
    }

    override fun updateBlockState() {

    }

    private fun onClickSettings() {

    }
}