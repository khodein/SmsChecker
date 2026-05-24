package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.IconButtonWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListeningToolbarWidget(
    modifier: Modifier = Modifier,
    state: ListeningToolbarState,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = state.title,
                style = SmsCheckerTheme.typography.titleLarge,
                color = SmsCheckerTheme.color.onSurface
            )
        },
        actions = {
            IconButtonWidget(
                imageVector = Icons.Default.Settings,
                tint = SmsCheckerTheme.color.onSurface,
                onClick = state.action.onClickSettings
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SmsCheckerTheme.color.surface
        )
    )
}