package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningConfigBlock(
    private val listeningConfigMapper: ListeningConfigMapper,
) : BaseBlock<ListeningConfigState, Unit>() {

    private val action = ListeningConfigAction(
        onClickEmpty = ::onClickEmpty,
        onClickItem = ::onClickItem,
    )

    override fun getInitialUiState(): ListeningConfigState {
        return listeningConfigMapper.mapConfigState(action = action)
    }

    override fun updateBlockState() {
        setState {
            listeningConfigMapper.mapConfigState(action = action)
        }
    }

    private fun onClickEmpty() {}

    private fun onClickItem(id: String) {}
}
