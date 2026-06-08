package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpTopBarMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        status: SmtpEmailStatus? = null,
    ): SmtpTopBarState {
        val titleRes = if (status == null) {
            R.string.feature_email_new_smtp
        } else {
            R.string.feature_email_update_smtp
        }
        return SmtpTopBarState(
            title = resProvider.getString(titleRes),
            statusLabel = resProvider.getString(R.string.feature_email_smtp_enable_sending),
            isStatus = status?.let { it == SmtpEmailStatus.Enable },
        )
    }
}