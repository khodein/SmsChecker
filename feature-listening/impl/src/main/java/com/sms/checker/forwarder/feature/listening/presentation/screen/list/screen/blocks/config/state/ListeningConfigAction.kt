package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ListeningConfigAction(
    val onClickEmpty: () -> Unit,
    val onClickConfig: (id: Long, type: ListeningConfigState.ConfigType) -> Unit,
    val onChangeConfigStatus: (
        id: Long,
        type: ListeningConfigState.ConfigType,
        isStatus: Boolean
    ) -> Unit,
)