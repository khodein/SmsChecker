package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigEvent
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningBottomBarMapper(
    private val resProvider: ResProvider
) {
    fun mapBottomBarCaption(): String {
        return resProvider.getString(R.string.feature_listening_add_forward)
    }

    fun mapSnackBarEventInfo(): ListeningConfigEvent.SnackBarEvent {
        return ListeningConfigEvent.SnackBarEvent(
            value = resProvider.getString(R.string.feature_listening_add_disabled_hint),
            status = ListeningConfigEvent.Status.Info
        )
    }
}