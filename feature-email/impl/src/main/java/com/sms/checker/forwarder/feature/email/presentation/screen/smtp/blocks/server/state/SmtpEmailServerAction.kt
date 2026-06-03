package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailServerAction(
    val onChangeHost: (value: String) -> Unit,
    val onChangePort: (value: String) -> Unit,
    val onChangeUserName: (value: String) -> Unit,
    val onChangePassword: (value: String) -> Unit,
)