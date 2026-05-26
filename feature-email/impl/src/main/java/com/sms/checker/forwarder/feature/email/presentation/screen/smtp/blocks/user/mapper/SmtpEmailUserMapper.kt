package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailUserMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        name: String,
        password: String,
        action: SmtpEmailUserAction,
    ): SmtpEmailUserState {
        val userNameState = SmtpEmailUserState.UserNameState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_username_placeholder),
            value = name,
        )
        val passwordState = SmtpEmailUserState.PasswordState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_password_placeholder),
            value = password,
        )
        return SmtpEmailUserState(
            title = resProvider.getString(R.string.feature_email_smtp_host_title),
            action = action,
            userNameState = userNameState,
            passwordState = passwordState,
        )
    }

    fun mapUserNameError(value: String): String? {
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