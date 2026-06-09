package com.sms.checker.forwarder.feature.listening.infrastructure.management.sending.facade

import com.sms.checker.forwarder.feature.email.domain.usecase.SendSmtpMessageUseCase
import com.sms.checker.forwarder.feature.email.infrastructure.SmtpEmailDelegate
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardStatus
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardType
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class ListeningSmtpFacade(
    private val sendSmtpMessageUseCase: SendSmtpMessageUseCase,
    private val smtpEmailDelegate: SmtpEmailDelegate,
): ListeningSendingFacade {

    private var provider: ListeningSendingFacade.Provider? = null

    override fun onCreate(provider: ListeningSendingFacade.Provider) {
        if (this.provider != null) return
        this.provider = provider
    }

    override fun onDestroy() {
        this.provider = null
    }

    override suspend fun send(
        model: SmsModel,
    ) = coroutineScope {
        val listIds = smtpEmailDelegate.getSmtpConfigList()
        val smsId = model.id ?: return@coroutineScope
        val listDeferred = listIds.map { smtpId ->
            async {
                runCatching {
                    sendSmtpMessageUseCase.invoke(
                        message = model.message,
                        smtpId = smtpId,
                    )
                }.onSuccess {
                    provider?.onResult(
                        params = ListeningSendingFacade.ResultParameters(
                            smsId = smsId,
                            configId = smtpId,
                            type = SmsForwardType.SMTP,
                            status = SmsForwardStatus.SUCCESS,
                            error = null,
                        )
                    )
                }.onFailure { throwable ->
                    provider?.onResult(
                        params = ListeningSendingFacade.ResultParameters(
                            smsId = smsId,
                            configId = smtpId,
                            type = SmsForwardType.SMTP,
                            status = SmsForwardStatus.ERROR,
                            error = throwable.message,
                        )
                    )
                }
            }
        }
        listDeferred.awaitAll()
    }
}