package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel

interface SetSmsForwardUseCase {
    suspend operator fun invoke(model: SmsForwardModel): Long
}