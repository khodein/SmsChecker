package com.sms.checker.forwarder.framework.uikit

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun CheckBoxWidget(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = SmsCheckerTheme.color
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = colors.primary,
            uncheckedColor = colors.outline,
            checkmarkColor = colors.onPrimary,
            disabledCheckedColor = colors.surfaceVariant,
            disabledUncheckedColor = colors.outlineVariant,
            disabledIndeterminateColor = colors.surfaceVariant,
        ),
    )
}
