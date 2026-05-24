package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningBottomBarMapper(
    private val resProvider: ResProvider
) {
    fun map(
        action: ListeningBottomBarAction,
    ): ListeningBottomBarState {
        return ListeningBottomBarState(
            caption = resProvider.getString(R.string.feature_listening_add_forward),
            action = action,
        )
    }
}