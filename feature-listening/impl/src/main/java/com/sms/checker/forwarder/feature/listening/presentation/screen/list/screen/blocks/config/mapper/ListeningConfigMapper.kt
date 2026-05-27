package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningConfigMapper(
    private val resProvider: ResProvider,
) {
    fun mapConfigState(
        action: ListeningConfigAction,
    ): ListeningConfigState {
        return ListeningConfigState.EmptyConfig(
            action = action,
            title = resProvider.getString(R.string.feature_listening_empty_configs),
            actionText = resProvider.getString(R.string.feature_listening_add_config),
        )
    }
}
