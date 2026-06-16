package com.sms.checker.forwarder.feature.listening.presentation.dialog

import androidx.compose.runtime.Composable
import com.sms.checker.forwarder.feature.listening.presentation.screen.warning.ListeningWarningScreen

@Composable
internal fun ListeningWarningRoute(
    title: String,
    description: String,
) {
    ListeningWarningScreen(
        title = title,
        description = description,
    )
}
