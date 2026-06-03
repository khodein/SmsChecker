package com.sms.checker.forwarder.framework.uikit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SnackBarPosition {
    Top,
    Bottom,
}

data class AppSnackBarVisuals(
    override val message: String,
    val type: SnackBarType,
    val position: SnackBarPosition = SnackBarPosition.Bottom,
    val durationMillis: Long = 4000L,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
) : SnackbarVisuals

suspend fun SnackbarHostState.showAppSnackBar(visuals: AppSnackBarVisuals) = coroutineScope {
    val dismissJob = launch {
        delay(visuals.durationMillis)
        currentSnackbarData?.dismiss()
    }
    showSnackbar(visuals)
    dismissJob.cancel()
}

suspend fun SnackbarHostState.showAppSnackBar(
    message: String,
    type: SnackBarType,
    position: SnackBarPosition = SnackBarPosition.Top,
    durationMillis: Long = 4000L,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Indefinite,
) = coroutineScope {
    val dismissJob = launch {
        delay(durationMillis)
        currentSnackbarData?.dismiss()
    }
    val visuals = AppSnackBarVisuals(
        message = message,
        type = type,
        position = position,
        durationMillis = durationMillis,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction,
        duration = duration
    )
    showSnackbar(visuals)
    dismissJob.cancel()
}

val LocalSnackBarHostState = compositionLocalOf { SnackbarHostState() }