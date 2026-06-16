package com.sms.checker.forwarder.feature.listening.delegate.management.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class NotificationDismissReceiver(
    private val onDismissed: () -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_NOTIFICATION_DISMISSED) return
        onDismissed.invoke()
    }

    companion object {
        const val ACTION_NOTIFICATION_DISMISSED =
            "com.sms.checker.forwarder.feature.listening.ACTION_NOTIFICATION_DISMISSED"
    }
}