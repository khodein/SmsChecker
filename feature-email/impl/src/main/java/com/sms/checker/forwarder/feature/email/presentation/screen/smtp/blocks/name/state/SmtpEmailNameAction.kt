package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailNameAction(
    val onChangeValue: (value: String) -> Unit
)