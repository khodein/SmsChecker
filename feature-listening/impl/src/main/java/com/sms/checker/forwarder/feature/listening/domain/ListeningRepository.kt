package com.sms.checker.forwarder.feature.listening.domain

import kotlinx.coroutines.flow.Flow

internal interface ListeningRepository {
    fun isListening(): Boolean
    fun observeListening(): Flow<Boolean>
    fun startListening()
    fun stopListening()
}