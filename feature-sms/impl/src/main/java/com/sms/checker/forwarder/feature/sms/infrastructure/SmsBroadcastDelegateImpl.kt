package com.sms.checker.forwarder.feature.sms.infrastructure

import android.content.Context
import android.content.IntentFilter
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.content.ContextCompat
import com.sms.checker.forwarder.feature.sms.domain.infrastructure.SmsBroadcastDelegate
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.infrastructure.mapper.SmsBroadcastMapper
import com.sms.checker.forwarder.feature.sms.infrastructure.receiver.SmsBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class SmsBroadcastDelegateImpl(
    private val setSmsUseCase: SetSmsUseCase,
    private val smsBroadcastMapper: SmsBroadcastMapper,
    private val context: Context,
) : SmsBroadcastDelegate {

    private var scope: CoroutineScope? = null
    private var receiver: SmsBroadcastReceiver? = null
    private var provider: SmsBroadcastDelegate.Provider? = null

    override fun onCreate(provider: SmsBroadcastDelegate.Provider) {
        if (receiver != null) return
        this.receiver = SmsBroadcastReceiver(::onReceiveSmsMessage)
        this.provider = provider
        this.scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onDestroy() {
        context.unregisterReceiver(receiver)
        this.scope?.cancel()
        this.scope = null
        this.provider = null
        this.receiver = null
    }

    private fun onReceiveSmsMessage(messages: List<SmsMessage>) {
        scope?.launch {
            runCatching {
                val model = smsBroadcastMapper.toModel(messages)
                setSmsUseCase.invoke(model)
            }.onSuccess { smsId ->
                provider?.onReceiveSmsId(smsId)
            }
        }
    }
}