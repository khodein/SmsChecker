package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailFromState(
    val title: String,
    val emailState: EmailState,
    val nameState: NameState,
) {
    @Immutable
    data class EmailState(
        val value: String,
        val placeholder: String,
        val error: String? = null,
    )

    @Immutable
    data class NameState(
        val value: String,
        val placeholder: String,
        val error: String? = null,
    )
}