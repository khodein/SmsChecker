package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningListAction(
    val onClickSettings: () -> Unit,
    val onClickAddForward: () -> Unit,
)
