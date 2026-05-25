package com.sms.checker.forwarder.feature.listening.data

import android.content.Context
import android.content.Intent
import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import com.sms.checker.forwarder.feature.listening.infrastructure.ListeningService

internal class ListeningRepositoryImpl(
    private val context: Context,
) : ListeningRepository {
    override fun isListening(): Boolean = ListeningService.isRunning.value

    override fun startListening() {
        context.startForegroundService(Intent(context, ListeningService::class.java))
    }

    override fun stopListening() {
        context.stopService(Intent(context, ListeningService::class.java))
    }
}