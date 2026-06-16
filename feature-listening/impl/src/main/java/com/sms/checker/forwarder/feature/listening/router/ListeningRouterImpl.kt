package com.sms.checker.forwarder.feature.listening.router

import com.sms.checker.forwarder.feature.listening.presentation.dialog.ListeningWarningKey
import com.sms.checker.forwarder.feature.listening.presentation.route.ListeningListKey
import com.sms.checker.forwarder.framework.router.Router

internal class ListeningRouterImpl(
    private val router: Router
) : ListeningRouter {
    override fun gotoListening() {
        router.goTo(ListeningListKey)
    }

    override fun gotoWarning(
        title: String,
        description: String
    ) {
        router.goTo(
            key = ListeningWarningKey(
                title = title,
                description = description,
            )
        )
    }
}