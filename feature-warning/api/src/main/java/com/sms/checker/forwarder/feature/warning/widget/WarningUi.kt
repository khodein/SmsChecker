package com.sms.checker.forwarder.feature.warning.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface WarningUi {

    @Composable
    fun WarningNotificationContent(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
        onClick: () -> Unit,
    )
}
