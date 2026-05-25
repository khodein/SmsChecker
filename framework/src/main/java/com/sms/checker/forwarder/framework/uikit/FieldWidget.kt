package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun FieldWidget(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = SmsCheckerTheme.color
    val typography = SmsCheckerTheme.typography

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label?.let { { Text(text = it, style = typography.bodyMedium) } },
        placeholder = placeholder?.let { { Text(text = it, style = typography.bodyMedium) } },
        isError = isError,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        textStyle = typography.bodyLarge,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            disabledTextColor = colors.onSurfaceVariant,
            errorTextColor = colors.onSurface,
            focusedContainerColor = colors.surfaceVariant,
            unfocusedContainerColor = colors.surfaceVariant,
            disabledContainerColor = colors.surfaceVariant,
            errorContainerColor = colors.surfaceVariant,
            cursorColor = colors.primary,
            errorCursorColor = colors.error,
            focusedIndicatorColor = colors.primary,
            unfocusedIndicatorColor = colors.outlineVariant,
            disabledIndicatorColor = colors.outlineVariant,
            errorIndicatorColor = colors.error,
            focusedLabelColor = colors.primary,
            unfocusedLabelColor = colors.onSurfaceVariant,
            disabledLabelColor = colors.onSurfaceVariant,
            errorLabelColor = colors.error,
            focusedPlaceholderColor = colors.onSurfaceVariant,
            unfocusedPlaceholderColor = colors.onSurfaceVariant,
            disabledPlaceholderColor = colors.onSurfaceVariant,
            errorPlaceholderColor = colors.onSurfaceVariant,
        ),
    )
}
