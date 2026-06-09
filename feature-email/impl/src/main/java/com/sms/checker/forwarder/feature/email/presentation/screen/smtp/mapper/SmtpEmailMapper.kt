package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailEvent
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailMapper(
    private val resProvider: ResProvider,
) {

    fun mapSuccessEvent(): SmtpEmailEvent {
        return SmtpEmailEvent(
            isSuccess = true,
            message = resProvider.getString(R.string.feature_email_smtp_save_success)
        )
    }

    fun mapErrorEvent(): SmtpEmailEvent {
        return SmtpEmailEvent(
            isSuccess = false,
            message = resProvider.getString(R.string.feature_email_smtp_save_error)
        )
    }

    fun mapDeleteSuccess(): SmtpEmailEvent {
        return SmtpEmailEvent(
            isSuccess = true,
            message = resProvider.getString(R.string.feature_email_smtp_delete_success)
        )
    }

    fun mapDeleteError(): SmtpEmailEvent {
        return SmtpEmailEvent(
            isSuccess = false,
            message = resProvider.getString(R.string.feature_email_smtp_delete_error)
        )
    }
}