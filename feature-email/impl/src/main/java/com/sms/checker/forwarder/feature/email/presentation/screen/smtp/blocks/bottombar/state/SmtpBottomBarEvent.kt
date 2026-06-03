package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiEvent

@Immutable
internal data class SmtpBottomBarEvent(
    val message: String,
    val type: Type
) : UiEvent {

    @Immutable
    enum class Type {
        Error,
        Success,
    }
}