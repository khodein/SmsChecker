package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailRecipientMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        email: String,
        name: String,
        action: SmtpEmailRecipientAction,
    ): SmtpEmailRecipientState {
        val emailState = SmtpEmailRecipientState.EmailState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_recipient_email_placeholder),
            value = email,
        )
        val nameState = SmtpEmailRecipientState.NameState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_recipient_name_placeholder),
            value = name,
        )
        return SmtpEmailRecipientState(
            title = resProvider.getString(R.string.feature_email_smtp_recipient_title),
            action = action,
            emailState = emailState,
            nameState = nameState,
        )
    }

    fun mapEmailError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_recipient_email_error_empty)
        } else {
            null
        }
    }

    fun mapNameError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_recipient_name_error_empty)
        } else {
            null
        }
    }
}
