package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningState(
    val title: String,
    val description: String,
    val needPermissionState: NeedPermissionState? = null,
    val isListening: Boolean? = null,
) {
    @Immutable
    data object NeedPermissionState
}