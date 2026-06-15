package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningState(
    val title: String,
    val description: String,
    val needPermissionState: NeedPermissionState? = null,
    val isListening: Boolean,
) {
    @Immutable
    internal data object NeedPermissionState
}