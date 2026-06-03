package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailRecipientState(
    val title: String,
    val emailState: EmailState,
    val subjectState: SubjectState,
) {
    @Immutable
    data class EmailState(
        val value: String,
        val placeholder: String,
        val error: String? = null
    )

    @Immutable
    data class SubjectState(
        val value: String,
        val placeholder: String,
        val error: String? = null
    )
}