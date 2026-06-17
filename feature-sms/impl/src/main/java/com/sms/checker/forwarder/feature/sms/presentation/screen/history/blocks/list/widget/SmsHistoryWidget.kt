package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun SmsHistoryWidget(
    modifier: Modifier,
    state: SmsHistoryListState.ItemState,
    onClick: (id: Long) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(state.id) }
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .padding(SmsCheckerTheme.padding.medium())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.extraSmall()),
            text = state.time,
            textAlign = TextAlign.End,
            style = SmsCheckerTheme.typography.labelMedium,
            color = SmsCheckerTheme.color.onSurfaceVariant
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.extraSmall()),
            textAlign = TextAlign.Start,
            text = state.title,
            style = SmsCheckerTheme.typography.titleMedium,
            color = SmsCheckerTheme.color.onSurface
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.extraSmall()),
            textAlign = TextAlign.Start,
            text = state.description,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurface
        )
    }
}