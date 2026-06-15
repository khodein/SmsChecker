package com.sms.checker.forwarder.framework.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sms.checker.forwarder.framework.uikit.LocalSnackBarHostState

object SmsCheckerTheme {

    val color: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val themeController: AppThemeController
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeController.current

    val languageController: AppLanguageController
        @Composable
        @ReadOnlyComposable
        get() = LocalAppLanguageController.current

    val corner: AppCorners
        @Composable
        @ReadOnlyComposable
        get() = LocalAppCorners.current

    val padding: AppPaddings
        @Composable
        @ReadOnlyComposable
        get() = LocalAppPaddings.current

    val snackBarHostState: SnackbarHostState
        @Composable
        @ReadOnlyComposable
        get() = LocalSnackBarHostState.current
}

@Composable
fun SmsCheckerTheme(
    initialTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val controller = remember { appThemeController(initialTheme) }
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (controller.current) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> systemDark
    }
    val colors = if (darkTheme) darkAppColors() else lightAppColors()

    val languageController = remember { appLanguageController() }
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalAppThemeController provides controller,
        LocalAppLanguageController provides languageController,
        LocalAppColors provides colors,
        LocalAppTypography provides AppTypography(),
        LocalAppCorners provides AppCorners(),
        LocalAppPaddings provides AppPaddings(),
        LocalSnackBarHostState provides snackbarHostState,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            content = content
        )
    }
}

private fun AppColors.toMaterialColorScheme(darkTheme: Boolean) = if (darkTheme) {
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        surfaceVariant = surfaceVariant,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        surfaceVariant = surfaceVariant,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError
    )
}

enum class AppTheme { LIGHT, DARK, SYSTEM }

interface AppThemeController {
    val current: AppTheme
    fun setTheme(theme: AppTheme)
}

@Stable
private class AppThemeControllerImpl(initial: AppTheme) : AppThemeController {
    private var _current by mutableStateOf(initial)
    override val current: AppTheme get() = _current
    override fun setTheme(theme: AppTheme) {
        _current = theme
    }
}

internal fun appThemeController(initial: AppTheme = AppTheme.SYSTEM): AppThemeController =
    AppThemeControllerImpl(initial)

internal val LocalAppThemeController = compositionLocalOf {
    appThemeController()
}
