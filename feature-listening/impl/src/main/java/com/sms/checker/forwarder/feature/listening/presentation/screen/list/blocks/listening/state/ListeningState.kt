package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningState(
    val title: String,
    val description: String,
    val isListening: Boolean,
    val needPermissionState: NeedPermissionState? = null,
    val notificationState: NotificationState? = null,
) {
    @Immutable
    data object NeedPermissionState

    @Immutable
    data class NotificationState(
        val title: String,
        val description: String,
    )
}