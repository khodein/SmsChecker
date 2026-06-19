package com.sms.checker.forwarder.feature.warning.router

import com.sms.checker.forwarder.feature.warning.presentation.route.warning.WarningKey
import com.sms.checker.forwarder.framework.router.Router

internal class WarningRouterImpl(
    private val router: Router,
) : WarningRouter {
    override fun gotoWarning() {
        router.goTo(WarningKey)
    }
}
