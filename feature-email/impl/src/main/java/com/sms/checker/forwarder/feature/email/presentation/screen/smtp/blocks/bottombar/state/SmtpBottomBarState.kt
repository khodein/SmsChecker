package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpBottomBarState(
    val caption: String,
    val action: SmtpBottomBarAction,
)