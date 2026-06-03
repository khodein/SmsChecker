package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiEvent

@Immutable
internal data class SmtpEmailEvent(
    val message: String,
    val isSuccess: Boolean,
) : UiEvent