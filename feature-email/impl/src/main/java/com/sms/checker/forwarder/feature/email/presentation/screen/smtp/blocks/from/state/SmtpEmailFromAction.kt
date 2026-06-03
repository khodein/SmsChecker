package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailFromAction(
    val onChangeEmail: (String) -> Unit,
    val onChangeName: (String) -> Unit,
)