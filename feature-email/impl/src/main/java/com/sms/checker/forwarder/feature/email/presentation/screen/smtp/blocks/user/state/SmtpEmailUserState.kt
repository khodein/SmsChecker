package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailUserState(
    val title: String,
    val action: SmtpEmailUserAction,
    val userNameState: UserNameState,
    val passwordState: PasswordState,
) {
    @Immutable
    data class UserNameState(
        val value: String,
        val placeholder: String,
        val error: String? = null
    )

    @Immutable
    data class PasswordState(
        val value: String,
        val placeholder: String,
        val error: String? = null
    )
}