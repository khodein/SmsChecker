package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpEmailNameMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        value: String,
        error: String?,
    ): SmtpEmailNameState {
        return SmtpEmailNameState(
            value = value,
            error = error,
            placeholder = resProvider.getString(R.string.feature_email_smtp_name_placeholder),
        )
    }

    fun mapError(
        value: String
    ): String? {
        val valueTrim = value.trim()
        val resId = when {
            valueTrim.isEmpty() -> R.string.feature_email_smtp_name_error_empty
            valueTrim.length < 3 -> R.string.feature_email_smtp_name_error_min_length
            else -> null
        }
        return resId?.let(resProvider::getString)
    }
}