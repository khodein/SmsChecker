package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpBottomBarMapper(
    private val resProvider: ResProvider,
) {
    fun map(
        action: SmtpBottomBarAction,
    ): SmtpBottomBarState {
        return SmtpBottomBarState(
            caption = resProvider.getString(R.string.feature_email_add_new),
            action = action,
            isEnabled = false
        )
    }
}