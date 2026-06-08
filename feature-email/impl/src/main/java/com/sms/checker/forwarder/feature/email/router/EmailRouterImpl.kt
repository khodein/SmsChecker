package com.sms.checker.forwarder.feature.email.router

import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailKey
import com.sms.checker.forwarder.framework.router.Router

internal class EmailRouterImpl(
    private val router: Router,
) : EmailRouter {
    override fun gotoSmtpEmail(id: Long?) {
        val key = SmtpEmailKey(id = id)
        router.goTo(key)
    }
}