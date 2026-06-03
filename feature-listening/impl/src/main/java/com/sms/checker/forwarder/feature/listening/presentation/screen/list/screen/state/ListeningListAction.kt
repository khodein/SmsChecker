package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarAction

@Immutable
internal data class ListeningListAction(
    val bottomBarAction: ListeningBottomBarAction,
    val configAction: ListeningConfigAction,
    val toolbarAction: ListeningToolbarAction,
    val listeningAction: ListeningAction,
)
