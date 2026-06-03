package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpTopBarMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        isUpdate: Boolean,
    ): SmtpTopBarState {
        return SmtpTopBarState(
            title = resProvider.getString(R.string.feature_email_new_smtp),
            notice = resProvider.getString(R.string.feature_email_smtp_instruction),
        )
    }
}