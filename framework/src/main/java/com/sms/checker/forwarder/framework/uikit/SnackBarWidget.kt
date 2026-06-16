package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

enum class SnackBarType {
    Error,
    Success,
    Info,
}

private fun SnackBarType.icon(): ImageVector = when (this) {
    SnackBarType.Error -> Icons.Rounded.Close
    SnackBarType.Success -> Icons.Rounded.CheckCircle
    SnackBarType.Info -> Icons.Rounded.Info
}

@Composable
private fun SnackBarType.iconColor(): Color = when (this) {
    SnackBarType.Error -> SmsCheckerTheme.color.error
    SnackBarType.Success -> Color(0xFF4CAF50)
    SnackBarType.Info -> SmsCheckerTheme.color.surface
}

@Composable
fun SnackBarWidget(
    text: String,
    type: SnackBarType,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onSwipeDismiss: (() -> Unit)? = null,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        SwipeToDismissBoxValue.Settled,
        SwipeToDismissBoxDefaults.positionalThreshold
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
            onSwipeDismiss?.invoke()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {},
        enableDismissFromStartToEnd = onSwipeDismiss != null,
        enableDismissFromEndToStart = onSwipeDismiss != null,
        modifier = modifier,
    ) {
        Snackbar(
            modifier = Modifier.defaultMinSize(minHeight = 60.dp),
            action = actionLabel?.let { label ->
                {
                    TextButton(onClick = { onAction?.invoke() }) {
                        Text(
                            text = label,
                            style = SmsCheckerTheme.typography.labelLarge,
                            color = SmsCheckerTheme.color.primary,
                        )
                    }
                }
            },
            dismissAction = onDismiss?.let { dismiss ->
                {
                    IconButton(onClick = dismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = SmsCheckerTheme.color.surface,
                        )
                    }
                }
            },
            shape = SmsCheckerTheme.corner.all(),
            containerColor = SmsCheckerTheme.color.onSurface,
            contentColor = SmsCheckerTheme.color.surface,
            actionContentColor = SmsCheckerTheme.color.primary,
            dismissActionContentColor = SmsCheckerTheme.color.surface,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.small()),
            ) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = type.icon(),
                    contentDescription = null,
                    tint = type.iconColor(),
                )
                Text(
                    text = text,
                    style = SmsCheckerTheme.typography.bodyMedium,
                    color = SmsCheckerTheme.color.surface,
                )
            }
        }
    }
}