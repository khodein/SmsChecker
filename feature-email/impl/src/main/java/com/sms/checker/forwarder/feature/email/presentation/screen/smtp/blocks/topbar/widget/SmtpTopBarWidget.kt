package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.SwitchWidget
import com.sms.checker.forwarder.framework.uikit.TopAppBarWidget

@Composable
internal fun SmtpTopBarWidget(
    modifier: Modifier = Modifier,
    state: SmtpTopBarState,
    action: SmtpTopBarAction,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.bottom()
            )
    ) {
        TopAppBarWidget(
            title = state.title,
            onClickBackPressed = action.onClickBackPressed
        )
        if (state.isStatus != null) {
            SmtpTopBarStatusWidget(
                modifier = Modifier.fillMaxWidth(),
                label = state.statusLabel,
                isStatus = state.isStatus,
                onChangeValue = action.onChangeValue,
            )
        }
    }
}

@Composable
private fun SmtpTopBarStatusWidget(
    modifier: Modifier = Modifier,
    label: String,
    isStatus: Boolean,
    onChangeValue: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = SmsCheckerTheme.padding.medium())
            .padding(bottom = SmsCheckerTheme.padding.small()),
        horizontalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.medium()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurface,
        )
        SwitchWidget(
            isValue = isStatus,
            onChangeValue = onChangeValue,
        )
    }
}