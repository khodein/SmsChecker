package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpTopBarState(
    val title: String,
    val statusLabel: String,
    val isStatus: Boolean? = null,
)