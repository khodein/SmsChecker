package com.sms.checker.forwarder.feature.sms.delegate

import kotlinx.coroutines.CoroutineScope

interface SmsBroadcastDelegate {

    fun onCreate(
        scope: CoroutineScope,
        provider: Provider
    )

    fun onDestroy()

    interface Provider {
        fun onReceiveSmsId(id: Long)
    }
}