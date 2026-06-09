package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

interface GetSmsByIdUseCase {
    suspend operator fun invoke(id: Long): SmsModel
}