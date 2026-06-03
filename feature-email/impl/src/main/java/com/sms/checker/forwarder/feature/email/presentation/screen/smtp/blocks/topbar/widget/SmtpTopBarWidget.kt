package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.widget

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
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
        SmtpTopBarNoticeWidget(
            modifier = Modifier.fillMaxWidth(),
            description = state.notice,
            onClick = action.onClickNotice
        )
    }
}

@Composable
private fun SmtpTopBarNoticeWidget(
    modifier: Modifier = Modifier,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
            .padding(paddingValues = SmsCheckerTheme.padding.horizontalMedium(),)
            .padding(bottom = SmsCheckerTheme.padding.medium())
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.medium()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = description,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurface
        )
        Icon(
            modifier = Modifier.size(24.dp),
            contentDescription = "Notice Icon",
            tint = SmsCheckerTheme.color.onSurfaceVariant,
            imageVector = Icons.Default.PlayArrow,
        )
    }
}