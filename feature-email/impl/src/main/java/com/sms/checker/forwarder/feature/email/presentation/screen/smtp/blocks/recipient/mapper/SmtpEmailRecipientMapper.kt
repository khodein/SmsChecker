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
        subject: String,
    ): SmtpEmailRecipientState {
        val emailState = SmtpEmailRecipientState.EmailState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_recipient_email_placeholder),
            value = email,
        )
        val subjectState = SmtpEmailRecipientState.SubjectState(
            placeholder = resProvider.getString(R.string.feature_email_smtp_recipient_subject_placeholder),
            value = subject,
        )
        return SmtpEmailRecipientState(
            title = resProvider.getString(R.string.feature_email_smtp_recipient_title),
            emailState = emailState,
            subjectState = subjectState,
        )
    }

    fun mapEmailError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_recipient_email_error_empty)
        } else {
            null
        }
    }

    fun mapSubjectError(value: String): String? {
        return if (value.trim().isEmpty()) {
            resProvider.getString(R.string.feature_email_smtp_recipient_subject_error_empty)
        } else {
            null
        }
    }
}
