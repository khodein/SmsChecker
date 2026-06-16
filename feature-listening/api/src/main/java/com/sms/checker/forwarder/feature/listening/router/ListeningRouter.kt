package com.sms.checker.forwarder.feature.listening.router

interface ListeningRouter {
    fun gotoListening()
    fun gotoWarning(
        title: String,
        description: String,
    )
}