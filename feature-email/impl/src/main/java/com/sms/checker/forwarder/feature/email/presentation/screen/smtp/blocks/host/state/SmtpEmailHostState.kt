package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailHostState(
    val action: SmtpEmailHostAction,
    val hostState: HostState,
    val portState: PortState,
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
}