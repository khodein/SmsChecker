package com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.screen

import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.screen.DevColorPaletteAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.screen.DevColorPaletteState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.router.Router

internal class DevColorPaletteViewModel(
    private val router: Router,
) : BaseViewModel<DevColorPaletteState>() {

    val action = DevColorPaletteAction(
        onBackPressed = ::onClickBackPressed
    )

    override fun getInitialUiState() = DevColorPaletteState(status = Status.SUCCESS)

    private fun onClickBackPressed() {
        router.goBack()
    }
}