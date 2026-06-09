package com.sms.checker.forwarder.feature.email.infrastructure

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpConfigUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpIdsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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