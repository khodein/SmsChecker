package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.mapper

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListItemState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningListMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        configState: ListeningConfigState
    ): List<ListeningListItemState> {
        return listOf(
            getConfigItemState(configState = configState)
        )
    }

    private fun getConfigItemState(
        configState: ListeningConfigState
    ): ListeningListItemState.ConfigItemState {
        return ListeningListItemState.ConfigItemState(
            id = "listening_config_state_id",
            contentType = "listening_config_state_content_type",
            listeningConfigState = configState,
        )
    }
}