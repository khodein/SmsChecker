package com.sms.checker.forwarder.feature.listening.router

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.route.ListeningListKey
import com.sms.checker.forwarder.framework.router.Router

internal class ListeningRouterImpl(
    private val router: Router
) : ListeningRouter {
    override fun gotoListening() {
        router.goTo(ListeningListKey)
    }
}