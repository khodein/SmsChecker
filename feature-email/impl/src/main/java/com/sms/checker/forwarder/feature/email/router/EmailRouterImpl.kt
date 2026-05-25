package com.sms.checker.forwarder.feature.email.router

import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailKey
import com.sms.checker.forwarder.framework.router.Router

internal class EmailRouterImpl(
    private val router: Router,
) : EmailRouter {
    override fun gotoSmtpEmail() {
        router.goTo(SmtpEmailKey)
    }
}