package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListItemState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.widget.ListeningBottomBarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.widget.ListeningConfigWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.widget.ListeningToolbarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.widget.ListeningWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListAction
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun ListeningListScreen(
    state: ListeningListState,
    action: ListeningListAction,
) {
    Scaffold(
        topBar = {
            ListeningListAppBarWidget(
                modifier = Modifier.fillMaxWidth(),
                listeningState = state.listeningState,
                toolbarState = state.listeningToolbarState,
                action = action,
            )
        },
        bottomBar = {
            if (state.status == Status.SUCCESS) {
                ListeningBottomBarWidget(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.listeningBottomBarState,
                    action = action.bottomBarAction,
                )
            }
        }
    ) {
        ListeningListContent(
            modifier = Modifier.padding(it),
            status = state.status,
            items = state.items,
            configAction = action.configAction
        )
    }
}

@Composable
private fun ListeningListContent(
    modifier: Modifier = Modifier,
    status: Status,
    items: List<ListeningListItemState>,
    configAction: ListeningConfigAction,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (status) {
            Status.LOADING -> {

            }

            Status.IDLE -> {
                ListeningListSuccessContent(
                    modifier = Modifier.fillMaxSize(),
                    items = items,
                    configAction = configAction,
                )
            }

            Status.ERROR -> {

            }

            Status.SUCCESS -> {

            }
        }
    }
}

@Composable
private fun ListeningListSuccessContent(
    modifier: Modifier = Modifier,
    items: List<ListeningListItemState>,
    configAction: ListeningConfigAction,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = SmsCheckerTheme.padding.verticalExtraSmall(),
        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall())
    ) {
        items(
            items = items,
            key = { item -> item.id },
            contentType = { item -> item.contentType }
        ) { item ->
            when (item) {
                is ListeningListItemState.ConfigItemState -> {
                    ListeningConfigWidget(
                        modifier = Modifier,
                        state = item.listeningConfigState,
                        action = configAction,
                    )
                }
            }
        }
    }
}

@Composable
internal fun ListeningListAppBarWidget(
    modifier: Modifier = Modifier,
    listeningState: ListeningState,
    toolbarState: ListeningToolbarState,
    action: ListeningListAction,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.bottom()
            ),
    ) {
        ListeningToolbarWidget(
            modifier = Modifier.fillMaxWidth(),
            state = toolbarState,
            action = action.toolbarAction,
        )
        ListeningWidget(
            modifier = Modifier.fillMaxWidth(),
            state = listeningState,
            action = action.listeningAction,
        )
    }
}

