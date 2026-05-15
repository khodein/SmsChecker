package com.sms.checker.forwarder.framework.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppTheme { LIGHT, DARK, SYSTEM }

interface AppThemeController {
    val current: AppTheme
    fun setTheme(theme: AppTheme)
}

@Stable
private class AppThemeControllerImpl(initial: AppTheme) : AppThemeController {
    private var _current by mutableStateOf(initial)
    override val current: AppTheme get() = _current
    override fun setTheme(theme: AppTheme) { _current = theme }
}

internal fun appThemeController(initial: AppTheme = AppTheme.SYSTEM): AppThemeController =
    AppThemeControllerImpl(initial)

internal val LocalAppThemeController = compositionLocalOf<AppThemeController> {
    appThemeController()
}
