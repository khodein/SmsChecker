package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel

internal class SetSmsForwardUseCaseImpl(
    private val repository: SmsRepository,
) : SetSmsForwardUseCase {
    override suspend fun invoke(model: SmsForwardModel): Long {
        return repository.setSmsForward(model)
    }
}