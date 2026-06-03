package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailTestAction(
    val onClickReload: () -> Unit
)