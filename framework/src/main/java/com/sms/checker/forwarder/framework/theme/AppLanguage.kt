package com.sms.checker.forwarder.framework.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage(val tag: String) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    KAZAKH("kk")
}

interface AppLanguageController {
    val current: AppLanguage
    fun setLanguage(language: AppLanguage)
}

@Stable
private class AppLanguageControllerImpl(initial: AppLanguage) : AppLanguageController {
    private var _current by mutableStateOf(initial)
    override val current: AppLanguage get() = _current
    override fun setLanguage(language: AppLanguage) {
        _current = language
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag)
        )
    }
}

internal fun appLanguageController(initial: AppLanguage = AppLanguage.ENGLISH): AppLanguageController =
    AppLanguageControllerImpl(initial)

internal val LocalAppLanguageController = compositionLocalOf<AppLanguageController> {
    appLanguageController()
}
