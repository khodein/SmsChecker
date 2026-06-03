package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state.SmtpEmailFromState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailFromMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        email: String,
        name: String,
    ): SmtpEmailFromState {
        return SmtpEmailFromState(
            title = resProvider.getString(R.string.feature_email_smtp_from_title),
            emailState = SmtpEmailFromState.EmailState(
                value = email,
                placeholder = resProvider.getString(R.string.feature_email_smtp_from_email_placeholder),
            ),
            nameState = SmtpEmailFromState.NameState(
                value = name,
                placeholder = resProvider.getString(R.string.feature_email_smtp_from_name_placeholder),
            ),
        )
    }

    fun mapEmailError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_from_email_error_empty)
        } else {
            null
        }
    }

    fun mapNameError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_from_name_error_empty)
        } else {
            null
        }
    }
}