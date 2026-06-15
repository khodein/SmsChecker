package com.sms.checker.forwarder.feature.sms.delegate.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

internal class SmsBroadcastReceiver(
    private val onReceiveSmsMessage: (list: List<SmsMessage>) -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        val list = messages.mapNotNull { it }
        if (list.isEmpty()) return
        onReceiveSmsMessage.invoke(list)
    }
}