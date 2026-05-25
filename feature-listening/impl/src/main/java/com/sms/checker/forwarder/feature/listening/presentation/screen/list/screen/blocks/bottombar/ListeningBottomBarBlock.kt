package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningBottomBarBlock(
    private val listeningBottomBarMapper: ListeningBottomBarMapper,
) : BaseBlock<ListeningBottomBarState, Unit>() {

    private val action = ListeningBottomBarAction(
        onClickForward = ::onClickForward
    )

    override fun getInitialUiState(): ListeningBottomBarState {
        return listeningBottomBarMapper.map(
            action = action,
        )
    }

    private fun onClickForward() {

    }

    override fun updateBlockState() {

    }
}