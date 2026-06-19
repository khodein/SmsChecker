package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        isListening: Boolean,
        warning: WarningModel,
    ): ListeningState {
        return ListeningState(
            title = resProvider.getString(R.string.feature_listening_switch_title),
            description = resProvider.getString(R.string.feature_listening_switch_description),
            isListening = isListening,
            notificationState = if (isListening) {
                ListeningState.NotificationState(
                    title = warning.title,
                    description = warning.notificationText,
                )
            } else {
                null
            },
            needPermissionState = null,
        )
    }
}
