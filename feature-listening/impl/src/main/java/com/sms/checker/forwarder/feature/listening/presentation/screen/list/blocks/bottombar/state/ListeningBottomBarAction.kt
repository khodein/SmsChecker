package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state

import androidx.compose.runtime.Immutable

@Immutable
data class ListeningBottomBarAction(
   val onClickForward: () -> Unit,
)