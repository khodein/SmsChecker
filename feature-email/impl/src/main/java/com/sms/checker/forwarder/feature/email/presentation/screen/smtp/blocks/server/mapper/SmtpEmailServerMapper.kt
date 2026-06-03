package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailServerMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        hostValue: String,
        portValue: String,
        name: String,
        password: String,
    ): SmtpEmailServerState {
        val hostState = SmtpEmailServerState.HostState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_host_placeholder),
            value = hostValue,
        )
        val portState = SmtpEmailServerState.PortState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_port_placeholder),
            value = portValue,
        )
        val nameState = SmtpEmailServerState.UserState.NameState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_username_placeholder),
            value = name,
        )
        val passwordState = SmtpEmailServerState.UserState.PasswordState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_password_placeholder),
            value = password,
        )
        val userState = SmtpEmailServerState.UserState(
            nameState = nameState,
            passwordState = passwordState,
        )
        return SmtpEmailServerState(
            title = resProvider.getString(R.string.feature_email_smtp_host_title),
            hostState = hostState,
            portState = portState,
            userState = userState,
        )
    }

    fun mapHostError(
        hostValue: String,
        portValue: String,
    ): String? {
        val portTrim = portValue.trim()
        val hostTrim = hostValue.trim()
        val resId = when {
            hostTrim.isEmpty() -> R.string.feature_email_smtp_host_error_empty
            portTrim.isEmpty() -> R.string.feature_email_smtp_port_error_empty
            portTrim.length < 3 -> R.string.feature_email_smtp_port_error_min_length
            else -> null
        }
        return resId?.let(resProvider::getString)
    }

    fun mapNameError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_username_error_empty)
        } else {
            null
        }
    }

    fun mapPasswordError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_password_error_empty)
        } else {
            null
        }
    }
}