package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

internal class UpdateSmsUseCaseImpl(
    private val repository: SmsRepository,
): UpdateSmsUseCase {
    override suspend operator fun invoke(model: SmsModel) {
        return repository.updateSms(model)
    }
}