package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailHostAction(
    val onChangeHost: (value: String) -> Unit,
    val onChangePort: (value: String) -> Unit,
)