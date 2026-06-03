package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiEvent

@Immutable
internal sealed interface SmtpEmailTestEvent : UiEvent {

    @Immutable
    data class Notification(
        val message: String,
        val isSuccess: Boolean,
    ) : SmtpEmailTestEvent

    @Immutable
    data object Scroll : SmtpEmailTestEvent
}