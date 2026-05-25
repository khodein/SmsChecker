package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailNameState(
    val value: String,
    val placeholder: String,
    val error: String? = null,
    val action: SmtpEmailNameAction,
)