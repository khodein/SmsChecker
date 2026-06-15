package com.sms.checker.forwarder.feature.email.delegate

import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpIdsUseCase

internal class SmtpEmailDelegateImpl(
    private val getEnabledSmtpIdsUseCase: GetEnabledSmtpIdsUseCase,
) : SmtpEmailDelegate {

    private var smtpList: List<Long>? = null

    override suspend fun getSmtpConfigList(): List<Long> {
        return smtpList ?: return runCatching {
            getEnabledSmtpIdsUseCase.invoke()
        }.getOrDefault(emptyList()).also { list ->
            this.smtpList = list
        }
    }
}