package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningToolbarMapper(
    private val resProvider: ResProvider
) {
    fun map(
        action: ListeningToolbarAction,
    ): ListeningToolbarState {
        return ListeningToolbarState(
            title = resProvider.getString(R.string.feature_listening_title),
            action = action,
        )
    }
}