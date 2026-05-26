package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailUserAction(
    val onChangeUserName: (value: String) -> Unit,
    val onChangePassword: (value: String) -> Unit
)