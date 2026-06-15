package com.sms.checker.forwarder.feature.listening.delegate.management

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningNotificationDelegate(
    private val resProvider: ResProvider,
    private val context: Context,
) {

    fun onCreate() {}

    fun onDestroy() {}

    fun getNotification(): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(resProvider.getString(R.string.feature_listening_notification_title))
            .setContentText(resProvider.getString(R.string.feature_listening_notification_text))
            .setSmallIcon(R.drawable.ic_notification_listening_circle)
            .setOngoing(true)
            .setContentIntent(getPendingIntent())
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            resProvider.getString(R.string.feature_listening_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private companion object {
        const val CHANNEL_ID = "sms_listening_notification_channel"
    }
}