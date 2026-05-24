package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningBottomBarBlock(
    private val listeningBottomBarMapper: com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.mapper.ListeningBottomBarMapper,
): BaseBlock<com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState, Unit>() {

    private val action =
        _root_ide_package_.com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction(
            onClickForward = ::onClickForward
        )

    override fun getInitialUiState(): com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState {
        return listeningBottomBarMapper.map(
            action = action,
        )
    }

    private fun onClickForward() {

    }

    override fun updateBlockState() {

    }
}