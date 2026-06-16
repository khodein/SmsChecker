package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningMapper(
    private val resProvider: ResProvider
) {
    fun map(
        isListening: Boolean,
    ): ListeningState {
        return ListeningState(
            title = resProvider.getString(R.string.feature_listening_switch_title),
            description = resProvider.getString(R.string.feature_listening_switch_description),
            isListening = isListening,
            notificationState = getNotificationState(isListening),
            needPermissionState = null,
        )
    }

    private fun getNotificationState(
        isListening: Boolean
    ): ListeningState.NotificationState? {
        return if (isListening) {
            ListeningState.NotificationState(
                title = resProvider.getString(R.string.feature_listening_notification_warning_title),
                description = resProvider.getString(R.string.feature_listening_notification_warning_text)
            )
        } else {
            null
        }
    }

    fun getDialogText(): Pair<String, String> {
        val title = resProvider.getString(R.string.feature_listening_notification_warning_title)
        val description =
            resProvider.getString(R.string.feature_listening_warning_dialog_description)
        return title to description
    }
}