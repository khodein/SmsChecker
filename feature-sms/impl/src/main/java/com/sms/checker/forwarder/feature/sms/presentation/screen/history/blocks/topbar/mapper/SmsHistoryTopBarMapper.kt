package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.mapper

import com.sms.checker.forwarder.feature.sms.R
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SmsHistoryTopBarMapper(
    private val resProvider: ResProvider
) {
    fun mapTitle(): String {
        return resProvider.getString(R.string.feature_sms_history_title)
    }
}