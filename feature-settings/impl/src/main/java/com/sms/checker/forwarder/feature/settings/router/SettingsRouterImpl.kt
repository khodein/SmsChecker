package com.sms.checker.forwarder.feature.settings.router

import com.sms.checker.forwarder.feature.settings.presentation.route.SettingsListKey
import com.sms.checker.forwarder.framework.router.Router

internal class SettingsRouterImpl(
    private val router: Router,
) : SettingsRouter {
    override fun gotoSettings() {
        router.goTo(SettingsListKey)
    }
}
