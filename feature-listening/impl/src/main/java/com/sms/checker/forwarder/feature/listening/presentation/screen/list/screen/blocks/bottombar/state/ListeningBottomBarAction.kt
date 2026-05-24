package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state

import androidx.compose.runtime.Immutable

@Immutable
data class ListeningBottomBarAction(
   val onClickForward: () -> Unit,
)