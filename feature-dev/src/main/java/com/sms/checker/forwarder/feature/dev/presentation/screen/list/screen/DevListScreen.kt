package com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListState
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen.widget.DevMenuWidget
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun DevListScreen(
    state: DevListState,
    action: DevListAction,
) {
    Scaffold(
        topBar = {

        },
        containerColor = SmsCheckerTheme.color.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                items = state.items,
                key = { it.id },
                contentType = { "DevListItemStateContentType" }
            ) { item ->
                DevMenuWidget(
                    name = item.name,
                    onClick = {
                        action.onClickDevListItem.invoke(item.id)
                    }
                )
            }
        }
    }
}