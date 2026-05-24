package com.sms.checker.forwarder.feature.listening.infrastructure.management

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

    fun onCreate() {
        createNotificationChannel()
    }

    fun onDestroy() {
        context.getSystemService(NotificationManager::class.java)
            .deleteNotificationChannel(CHANNEL_ID)
    }

    fun getNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(resProvider.getString(R.string.feature_listening_notification_title))
            .setContentText(resProvider.getString(R.string.feature_listening_notification_text))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setContentIntent(getPendingIntent())
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            resProvider.getString(R.string.feature_listening_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
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
        const val CHANNEL_ID = "sms_listening_channel"
    }
}