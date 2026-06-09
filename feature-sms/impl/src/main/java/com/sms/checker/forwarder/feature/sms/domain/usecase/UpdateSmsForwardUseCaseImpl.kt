package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel

internal class UpdateSmsForwardUseCaseImpl(
    private val repository: SmsRepository,
) : UpdateSmsForwardUseCase {
    override suspend fun invoke(model: SmsForwardModel) {
        repository.updateSmsForward(model)
    }
}