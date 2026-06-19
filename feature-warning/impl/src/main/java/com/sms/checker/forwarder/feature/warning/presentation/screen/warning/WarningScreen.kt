package com.sms.checker.forwarder.feature.warning.presentation.screen.warning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state.WarningAction
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state.WarningState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun WarningScreen(
    modifier: Modifier = Modifier,
    state: WarningState,
    @Suppress("UNUSED_PARAMETER") action: WarningAction,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SmsCheckerTheme.padding.horizontalMedium())
            .padding(SmsCheckerTheme.padding.verticalLarge()),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.large()),
            text = state.title,
            style = SmsCheckerTheme.typography.titleLarge,
            color = SmsCheckerTheme.color.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = state.description,
            textAlign = TextAlign.Start,
            style = SmsCheckerTheme.typography.bodyLarge,
            color = SmsCheckerTheme.color.onSurface,
        )
    }
}
