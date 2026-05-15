package com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun DevMenuWidget(
    name: String,
    onClick: () -> Unit
) {
    val colors = SmsCheckerTheme.color
    val typography = SmsCheckerTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick.invoke()
            })
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = typography.bodyLarge,
            color = colors.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}