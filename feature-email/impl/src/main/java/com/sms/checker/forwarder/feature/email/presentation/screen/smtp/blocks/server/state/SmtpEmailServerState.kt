package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailServerState(
    val title: String,
    val hostState: HostState,
    val portState: PortState,
    val userState: UserState,
) {
    @Immutable
    data class HostState(
        val placeholder: String,
        val value: String,
        val error: String? = null,
    )

    @Immutable
    data class PortState(
        val placeholder: String,
        val value: String,
        val error: String? = null,
    )

    @Immutable
    data class UserState(
        val nameState: NameState,
        val passwordState: PasswordState,
    ) {
        @Immutable
        data class NameState(
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
}