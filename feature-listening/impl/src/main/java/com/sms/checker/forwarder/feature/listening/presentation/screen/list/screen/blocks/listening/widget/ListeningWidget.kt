package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun ListeningWidget(
    modifier: Modifier = Modifier,
    state: ListeningState,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(24.dp),
            tint = if (state.isListening == true) {
                SmsCheckerTheme.color.primary
            } else {
                SmsCheckerTheme.color.onSurfaceVariant
            },
            imageVector = Icons.Default.Email,
            contentDescription = "Listening Switch Icon"
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.title,
                style = SmsCheckerTheme.typography.bodyLarge,
                color = SmsCheckerTheme.color.onSurface
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = state.description,
                style = SmsCheckerTheme.typography.bodyMedium,
                color = SmsCheckerTheme.color.onSurfaceVariant
            )
        }
        Switch(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = state.isListening ?: false,
            colors = SwitchDefaults.colors().copy(
                checkedThumbColor = SmsCheckerTheme.color.onPrimary,
                checkedTrackColor = SmsCheckerTheme.color.primary,
                uncheckedThumbColor = SmsCheckerTheme.color.onSurfaceVariant,
                uncheckedTrackColor = SmsCheckerTheme.color.surfaceVariant,
                uncheckedBorderColor = SmsCheckerTheme.color.outline,
            ),
            onCheckedChange = state.action.onClickListening
        )
    }
}