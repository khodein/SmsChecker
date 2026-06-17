package com.sms.checker.forwarder.feature.sms.router

import com.sms.checker.forwarder.feature.sms.presentation.route.history.SmsHistoryKey
import com.sms.checker.forwarder.framework.router.Router

internal class SmsRouterImpl(
    private val router: Router,
) : SmsRouter {
    override fun gotoSmsHistory() {
        router.goTo(SmsHistoryKey)
    }
}
