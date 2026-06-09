package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository

internal class DeleteSmtpConfigByIdUseCaseImpl(
    private val repository: EmailRepository
) : DeleteSmtpConfigByIdUseCase {
    override suspend fun invoke(id: Long) {
        repository.deleteSmtpConfigById(id)
    }
}