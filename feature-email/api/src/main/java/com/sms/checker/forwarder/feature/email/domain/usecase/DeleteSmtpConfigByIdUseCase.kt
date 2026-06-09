package com.sms.checker.forwarder.feature.email.domain.usecase

interface DeleteSmtpConfigByIdUseCase {
    suspend operator fun invoke(id: Long)
}