package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

internal class SetSmsUseCase(
    private val repository: SmsRepository,
) {
    suspend fun invoke(model: SmsModel): Long {
        return repository.setSms(model)
    }
}