package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper

import com.sms.checker.forwarder.feature.email.R
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmtpBottomBarMapper(
    private val resProvider: ResProvider,
) {
    fun map(isUpdate: Boolean): SmtpBottomBarState {
        val resId = if (isUpdate) {
            R.string.feature_email_update
        } else {
            R.string.feature_email_add_new
        }
        return SmtpBottomBarState(
            caption = resProvider.getString(resId),
        )
    }

    fun getErrorEvent(): SmtpBottomBarEvent {
        return SmtpBottomBarEvent(
            message = resProvider.getString(
                R.string.feature_email_smtp_fields_invalid,
            ),
            type = SmtpBottomBarEvent.Type.Error,
        )
    }

    fun onSuccessEvent(): SmtpBottomBarEvent {
        return SmtpBottomBarEvent(
            message = resProvider.getString(
                R.string.feature_email_smtp_fields_valid
            ),
            type = SmtpBottomBarEvent.Type.Success
        )
    }
}