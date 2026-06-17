package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state

import androidx.compose.runtime.Immutable

@Immutable
data class SmsHistoryTopBarAction(
    val onClickBack: () -> Unit
)