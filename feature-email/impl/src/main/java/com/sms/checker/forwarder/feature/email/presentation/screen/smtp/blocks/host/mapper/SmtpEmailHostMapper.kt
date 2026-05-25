package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailHostMapper(
    private val resProvider: ResProvider
) {
    fun map(
        action: SmtpEmailHostAction,
        hostValue: String,
        portValue: String,
    ): SmtpEmailHostState {
        val hostState = SmtpEmailHostState.HostState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_host_placeholder),
            value = hostValue,
        )
        val portState = SmtpEmailHostState.PortState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_port_placeholder),
            value = portValue,
        )
        return SmtpEmailHostState(
            action = action,
            hostState = hostState,
            portState = portState
        )
    }

    fun mapHostError(
        value: String,
    ): String? {
        val valueTrim = value.trim()
        return if (valueTrim.isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_host_error_empty)
        } else {
            null
        }
    }

    fun mapPortError(
        value: String,
    ): String? {
        val valueTrim = value.trim()
        return if (valueTrim.isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_port_error_empty)
        } else {
            null
        }
    }
}