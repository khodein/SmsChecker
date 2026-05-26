package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserState

@Immutable
internal sealed interface SmtpEmailItemState {
    val id: String
    val contentType: String

    @Immutable
    data class NameState(
        override val id: String,
        override val contentType: String,
        val state: SmtpEmailNameState,
    ) : SmtpEmailItemState

    @Immutable
    data class HostState(
        override val id: String,
        override val contentType: String,
        val state: SmtpEmailHostState,
    ): SmtpEmailItemState

    @Immutable
    data class UserState(
        override val id: String,
        override val contentType: String,
        val state: SmtpEmailUserState,
    ): SmtpEmailItemState
}