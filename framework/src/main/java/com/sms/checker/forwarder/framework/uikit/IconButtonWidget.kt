package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun IconButtonWidget(
    modifier: Modifier = Modifier,
    tint: Color,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        content = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = imageVector,
                contentDescription = "Icon Button Widget",
                tint = tint,
            )
        },
        onClick = onClick
    )
}