package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state

import androidx.compose.runtime.Immutable

@Immutable
data class ListeningToolbarAction(
    val onClickSettings: () -> Unit,
)