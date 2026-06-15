package com.sms.checker.forwarder.feature.sms.delegate

interface SmsBroadcastDelegate {

    fun onCreate(provider: Provider)

    fun onDestroy()

    interface Provider {
        fun onReceiveSmsId(id: Long)
    }
}