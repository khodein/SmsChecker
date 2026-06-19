package com.sms.checker.forwarder.feature.warning.presentation.screen.warning.mapper

import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state.WarningState

internal class WarningScreenMapper {

    fun map(model: WarningModel): WarningState = WarningState(
        title = model.title,
        description = model.description,
    )
}
