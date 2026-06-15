package com.sms.checker.forwarder.feature.listening.delegate.management.sending

import com.sms.checker.forwarder.feature.listening.delegate.management.sending.facade.ListeningSendingFacade
import com.sms.checker.forwarder.feature.listening.delegate.management.sending.facade.ListeningSmtpFacade
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsByIdUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.SetSmsForwardUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class ListeningSendingDelegate(
    private val getSmsByIdUseCase: GetSmsByIdUseCase,
    private val setSmsForwardUseCase: SetSmsForwardUseCase,
    listeningSmtpSendingDelegate: ListeningSmtpFacade,
): ListeningSendingFacade.Provider {
    private var scope: CoroutineScope? = null

    private val facades = listOf(
        listeningSmtpSendingDelegate
    )

    fun onCreate() {
        if (scope != null) return
        this.scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        facades.forEach {
            it.onCreate(this)
        }
    }

    fun send(smsId: Long) {
        scope?.launch {
            runCatching {
                getSmsByIdUseCase.invoke(smsId)
            }.onSuccess(::send)
        }
    }

    fun onDestroy() {
        facades.forEach {
            it.onDestroy()
        }
        scope?.cancel()
        scope = null
    }

    private fun send(model: SmsModel) {
        scope?.launch {
            val sendDeferred = facades.map {
                async {
                    it.send(model)
                }
            }
            sendDeferred.awaitAll()
        }
    }

    override suspend fun onResult(params: ListeningSendingFacade.ResultParameters) {
        runCatching {
            setSmsForwardUseCase.invoke(
                model = SmsForwardModel(
                    smsId = params.smsId,
                    type = params.type,
                    configId = params.configId,
                    status = params.status,
                    error = params.error,
                    attemptedAt = System.currentTimeMillis(),
                )
            )
        }
    }
}