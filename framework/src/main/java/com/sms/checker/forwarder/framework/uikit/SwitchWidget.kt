package com.sms.checker.forwarder.framework.uikit

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun SwitchWidget(
    isValue: Boolean,
    onChangeValue: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = isValue,
        onCheckedChange = onChangeValue,
        modifier = modifier,
        colors = SwitchDefaults.colors().copy(
            checkedThumbColor = SmsCheckerTheme.color.onPrimary,
            checkedTrackColor = SmsCheckerTheme.color.primary,
            uncheckedThumbColor = SmsCheckerTheme.color.onSurfaceVariant,
            uncheckedTrackColor = SmsCheckerTheme.color.surfaceVariant,
            uncheckedBorderColor = SmsCheckerTheme.color.outline,
        ),
    )
}
