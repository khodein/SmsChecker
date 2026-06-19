package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.mapper

import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningState
import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel

internal class SmsHistoryWarningMapper {

    fun map(
        isListening: Boolean,
        isHistoryNotEmpty: Boolean,
        warning: WarningModel,
    ): SmsHistoryWarningState = SmsHistoryWarningState(
        isVisible = isListening && isHistoryNotEmpty,
        title = warning.title,
        description = warning.notificationText,
    )
}
