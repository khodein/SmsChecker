package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningConfigAction(
    val onClickEmpty: () -> Unit,
    val onClickItem: (id: String) -> Unit
)