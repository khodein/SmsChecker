package com.sms.checker.forwarder.feature.settings.presentation.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.state.SettingsListAction
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.state.SettingsListState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.TopAppBarWidget

@Composable
internal fun SettingsListScreen(
    state: SettingsListState,
    action: SettingsListAction,
) {
    Scaffold(
        topBar = {
            TopAppBarWidget(
                title = state.title,
                onClickBackPressed = action.onClickBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(SmsCheckerTheme.padding.allLarge()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.emptyTitle,
                style = SmsCheckerTheme.typography.titleLarge,
                color = SmsCheckerTheme.color.onBackground
            )
            Text(
                modifier = Modifier.padding(top = SmsCheckerTheme.padding.small()),
                text = state.emptyDescription,
                style = SmsCheckerTheme.typography.bodyMedium,
                color = SmsCheckerTheme.color.onSurfaceVariant
            )
        }
    }
}
