package com.sms.checker.forwarder.feature.listening.presentation.screen.list.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.state.ListeningToolbarAction

@Immutable
internal data class ListeningListAction(
    val bottomBarAction: ListeningBottomBarAction,
    val configAction: ListeningConfigAction,
    val toolbarAction: ListeningToolbarAction,
    val historyAction: ListeningHistoryAction,
    val listeningAction: ListeningAction,
)
