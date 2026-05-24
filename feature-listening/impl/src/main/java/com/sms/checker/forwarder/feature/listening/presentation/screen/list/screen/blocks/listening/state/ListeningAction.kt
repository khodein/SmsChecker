package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningAction(
    val onClickListening: (isListening: Boolean) -> Unit,
    val onPermissionListeningResult: (isGranted: Boolean) -> Unit,
)