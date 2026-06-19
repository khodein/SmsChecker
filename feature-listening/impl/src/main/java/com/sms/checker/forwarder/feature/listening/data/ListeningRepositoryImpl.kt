package com.sms.checker.forwarder.feature.listening.data

import android.content.Context
import android.content.Intent
import com.sms.checker.forwarder.feature.listening.delegate.ListeningService
import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import kotlinx.coroutines.flow.Flow

internal class ListeningRepositoryImpl(
    private val context: Context,
) : ListeningRepository {

    private val _isListening by ListeningService::isRunning

    override fun isListening(): Boolean = _isListening.value

    override fun observeListening(): Flow<Boolean> {
        return _isListening
    }

    override fun startListening() {
        context.startForegroundService(Intent(context, ListeningService::class.java))
    }

    override fun stopListening() {
        context.stopService(Intent(context, ListeningService::class.java))
    }
}