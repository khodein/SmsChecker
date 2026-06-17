package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmsHistoryListAction(
    val onClickReload: (page: Int) -> Unit,
    val onClickItem: (id: Long) -> Unit,
    val onNextLoad: (page: Int) -> Unit
)