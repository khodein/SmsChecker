package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

internal class GetSmsByIdUseCaseImpl(
    private val repository: SmsRepository,
) : GetSmsByIdUseCase {
    override suspend fun invoke(id: Long): SmsModel {
        return repository.getById(id)
    }
}