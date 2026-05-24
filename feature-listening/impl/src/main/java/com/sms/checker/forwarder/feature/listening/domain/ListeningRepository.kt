package com.sms.checker.forwarder.feature.listening.domain

import kotlinx.coroutines.flow.Flow

internal interface ListeningRepository {
    fun observeListening(): Flow<Boolean>
    fun isListening(): Boolean
    fun startListening()
    fun stopListening()
}