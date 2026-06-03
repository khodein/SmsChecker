package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailRecipientAction(
    val onChangeEmail: (value: String) -> Unit,
    val onChangeSubject: (value: String) -> Unit,
)