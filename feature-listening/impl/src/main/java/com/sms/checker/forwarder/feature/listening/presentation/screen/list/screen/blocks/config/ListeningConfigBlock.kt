package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningConfigBlock(
    private val listeningConfigMapper: ListeningConfigMapper,
) : BaseBlock<ListeningConfigState, ListeningConfigAction, Unit>() {

    override val action = ListeningConfigAction(
        onClickEmpty = ::onClickEmpty,
        onClickItem = ::onClickItem,
    )

    override fun getInitialUiState(): ListeningConfigState {
        return listeningConfigMapper.mapConfigState()
    }

    override fun updateBlockState() {
        setState {
            listeningConfigMapper.mapConfigState()
        }
    }

    private fun onClickEmpty() {}

    private fun onClickItem(id: String) {}
}
