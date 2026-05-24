package com.sms.checker.forwarder.feature.listening.data

import android.content.Context
import android.content.Intent
import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import com.sms.checker.forwarder.feature.listening.infrastructure.ListeningService
import kotlinx.coroutines.flow.Flow

internal class ListeningRepositoryImpl(
    private val context: Context,
) : ListeningRepository {

    override fun observeListening(): Flow<Boolean> = ListeningService.isRunning
    override fun isListening(): Boolean = ListeningService.isRunning.value

    override fun startListening() {
        context.startForegroundService(Intent(context, ListeningService::class.java))
    }

    override fun stopListening() {
        context.stopService(Intent(context, ListeningService::class.java))
    }
}