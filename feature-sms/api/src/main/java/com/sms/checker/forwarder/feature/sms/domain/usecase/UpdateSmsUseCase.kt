package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

interface UpdateSmsUseCase {
    suspend operator fun invoke(model: SmsModel)
}