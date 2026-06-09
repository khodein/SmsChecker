package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

internal class SetSmsUseCaseImpl(
    private val repository: SmsRepository,
): SetSmsUseCase {
    override suspend fun invoke(model: SmsModel): Long {
        return repository.setSms(model)
    }
}