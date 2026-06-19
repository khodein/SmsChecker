package com.sms.checker.forwarder.feature.warning.domain.usecase

import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel

interface GetWarningUseCase {
    operator fun invoke(): WarningModel
}
