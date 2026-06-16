package com.sms.checker.forwarder.feature.listening.delegate

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.sms.checker.forwarder.feature.listening.delegate.management.notification.ListeningNotificationDelegate
import com.sms.checker.forwarder.feature.listening.delegate.management.sending.ListeningSendingDelegate
import com.sms.checker.forwarder.feature.sms.delegate.SmsBroadcastDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ListeningService : Service(),
    KoinComponent,
    SmsBroadcastDelegate.Provider,
    ListeningNotificationDelegate.Provider {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val listeningNotificationDelegate by inject<ListeningNotificationDelegate>()
    private val listeningSendingDelegate by inject<ListeningSendingDelegate>()
    private val smsBroadcastDelegate by inject<SmsBroadcastDelegate>()

    override fun onCreate() {
        super.onCreate()
        listeningNotificationDelegate.onCreate(this)
        smsBroadcastDelegate.onCreate(
            scope = scope,
            provider = this
        )
        listeningSendingDelegate.onCreate(scope = scope)
        _isRunning.value = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startService()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startService() {
        val notification = listeningNotificationDelegate.getNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        smsBroadcastDelegate.onDestroy()
        listeningNotificationDelegate.onDestroy()
        listeningSendingDelegate.onDestroy()
        _isRunning.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onReceiveSmsId(id: Long) {
        listeningSendingDelegate.send(id)
    }

    override fun onNotificationDismissed() {
        if (isRunning.value) {
            startService()
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private val _isRunning = MutableStateFlow(false)
        val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    }
}
