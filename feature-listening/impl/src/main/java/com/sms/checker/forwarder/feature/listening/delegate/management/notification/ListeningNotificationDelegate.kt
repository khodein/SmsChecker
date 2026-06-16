package com.sms.checker.forwarder.feature.listening.delegate.management.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.content.ContextCompat
import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.delegate.management.notification.receiver.NotificationDismissReceiver
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningNotificationDelegate(
    private val resProvider: ResProvider,
    private val context: Context,
) {
    private var notificationReceiver: NotificationDismissReceiver? = null
    private var provider: Provider? = null

    val notification: Notification
        get() = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(resProvider.getString(R.string.feature_listening_notification_title))
            .setContentText(resProvider.getString(R.string.feature_listening_notification_text))
            .setSmallIcon(R.drawable.ic_notification_listening_circle)
            .setOngoing(false)
            .setContentIntent(getPendingIntent())
            .setDeleteIntent(getDeletePendingIntent())
            .setPriority(PRIORITY_HIGH)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .build()

    fun onCreate(provider: Provider) {
        this.provider = provider
        createNotificationChannel()
        registerReceiver()
    }

    fun onDestroy() {
        unregisterReceiver()
        provider = null
    }

    private fun registerReceiver() {
        if (notificationReceiver != null) return
        notificationReceiver = NotificationDismissReceiver(::onNotificationDismissed).also {
            ContextCompat.registerReceiver(
                context,
                it,
                IntentFilter(NotificationDismissReceiver.ACTION_NOTIFICATION_DISMISSED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(notificationReceiver)
        notificationReceiver = null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            resProvider.getString(R.string.feature_listening_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        context
            .getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getDeletePendingIntent(): PendingIntent {
        val intent = Intent(NotificationDismissReceiver.ACTION_NOTIFICATION_DISMISSED)
            .setPackage(context.packageName)
        return PendingIntent.getBroadcast(
            context,
            DISMISS_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun onNotificationDismissed() {
        provider?.onNotificationDismissed()
    }

    interface Provider {
        fun onNotificationDismissed()
    }

    private companion object {
        const val CHANNEL_ID = "sms_listening_notification_channel"
        const val DISMISS_REQUEST_CODE = 1002
    }
}