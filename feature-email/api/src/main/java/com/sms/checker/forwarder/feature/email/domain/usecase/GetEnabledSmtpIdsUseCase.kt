package com.sms.checker.forwarder.feature.email.domain.usecase

interface GetEnabledSmtpIdsUseCase {
    suspend operator fun invoke(): List<Long>
}