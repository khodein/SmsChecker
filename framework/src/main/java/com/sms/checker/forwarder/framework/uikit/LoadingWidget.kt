package com.sms.checker.forwarder.framework.uikit

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun LoadingWidget(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = SmsCheckerTheme.color.primary,
        trackColor = SmsCheckerTheme.color.surfaceVariant,
    )
}