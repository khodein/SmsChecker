package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
fun NavigationBackWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButtonWidget(
        modifier = modifier,
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        tint = SmsCheckerTheme.color.onBackground,
        onClick = onClick
    )
}