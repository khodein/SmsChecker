package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmsHistoryWarningState(
    val isVisible: Boolean,
    val title: String,
    val description: String,
)
