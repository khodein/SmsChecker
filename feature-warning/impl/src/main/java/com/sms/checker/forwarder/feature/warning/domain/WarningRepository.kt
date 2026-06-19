package com.sms.checker.forwarder.feature.warning.domain

import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel

internal interface WarningRepository {
    fun getWarning(): WarningModel
}
