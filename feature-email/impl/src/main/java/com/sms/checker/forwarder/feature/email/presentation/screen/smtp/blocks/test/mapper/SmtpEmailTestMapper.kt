package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailTestMapper(
    private val resProvider: ResProvider,
) {
    fun getTestMessage(): String {
        return resProvider.getString(R.string.feature_email_smtp_test_message)
    }

    fun mapTitle(
        status: SmtpEmailTestState.TestStatus
    ): String {
        val resId = when (status) {
            SmtpEmailTestState.TestStatus.Loading -> R.string.feature_email_smtp_test_title_loading
            SmtpEmailTestState.TestStatus.Succuss -> R.string.feature_email_smtp_test_title_success
            SmtpEmailTestState.TestStatus.Error -> R.string.feature_email_smtp_test_title_error
            else -> null
        }
        return resId?.let(resProvider::getString).orEmpty()
    }

    fun mapNotice(
        status: SmtpEmailTestState.TestStatus
    ): String {
        val resId = when (status) {
            SmtpEmailTestState.TestStatus.Loading -> R.string.feature_email_smtp_test_status_loading
            SmtpEmailTestState.TestStatus.Succuss -> R.string.feature_email_smtp_test_status_success
            SmtpEmailTestState.TestStatus.Error -> R.string.feature_email_smtp_test_status_error
            else -> null
        }
        return resId?.let(resProvider::getString).orEmpty()
    }

    fun mapDescription(
        status: SmtpEmailTestState.TestStatus
    ): String {
        val resId = when (status) {
            SmtpEmailTestState.TestStatus.Loading -> R.string.feature_email_smtp_test_description_loading
            SmtpEmailTestState.TestStatus.Succuss -> R.string.feature_email_smtp_test_description_success
            SmtpEmailTestState.TestStatus.Error -> R.string.feature_email_smtp_test_description_error
            else -> null
        }
        return resId?.let(resProvider::getString).orEmpty()
    }

    fun mapButton(): String {
        return resProvider.getString(R.string.feature_email_smtp_send_again)
    }

    fun mapSuccessEvent(): SmtpEmailTestEvent {
        return SmtpEmailTestEvent.Notification(
            message = resProvider.getString(R.string.feature_email_smtp_test_send_success),
            isSuccess = true
        )
    }

    fun mapErrorEvent(): SmtpEmailTestEvent {
        return SmtpEmailTestEvent.Notification(
            message = resProvider.getString(R.string.feature_email_smtp_test_send_error,),
            isSuccess = false
        )
    }
}