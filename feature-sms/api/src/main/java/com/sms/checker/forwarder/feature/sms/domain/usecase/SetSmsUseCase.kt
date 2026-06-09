package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

interface SetSmsUseCase {
    suspend operator fun invoke(model: SmsModel): Long
}