package com.sms.checker.forwarder.feature.warning.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

private val WarningRed = Color(0xFFEF4444)
private val OnWarningRed = Color(0xFFFFFFFF)

internal class WarningUiImpl : WarningUi {

    @Composable
    override fun WarningNotificationContent(
        modifier: Modifier,
        title: String,
        description: String,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .background(
                    shape = SmsCheckerTheme.corner.all(),
                    color = WarningRed,
                )
                .padding(SmsCheckerTheme.padding.medium()),
            verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.small()),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = SmsCheckerTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = OnWarningRed,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = SmsCheckerTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = OnWarningRed,
            )
        }
    }
}
