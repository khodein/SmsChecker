package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningHistoryAction(
    val onClickItem: (id: Long) -> Unit,
    val onClickAll: () -> Unit,
    val onClickReload: () -> Unit
)