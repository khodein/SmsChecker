package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningBottomBarState(
    val caption: String,
    val isVisible: Boolean,
)