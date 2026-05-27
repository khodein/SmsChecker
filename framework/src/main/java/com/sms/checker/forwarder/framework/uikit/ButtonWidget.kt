package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun DefaultButtonWidget(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    caption: String,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit = {
        Text(
            text = caption,
            style = SmsCheckerTheme.typography.labelLarge,
            color = SmsCheckerTheme.color.onPrimary,
        )
    },
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = SmsCheckerTheme.color.primary,
            contentColor = SmsCheckerTheme.color.onPrimary,
            disabledContainerColor = SmsCheckerTheme.color.surfaceVariant,
            disabledContentColor = SmsCheckerTheme.color.onSurfaceVariant,
        ),
        content = content,
    )
}