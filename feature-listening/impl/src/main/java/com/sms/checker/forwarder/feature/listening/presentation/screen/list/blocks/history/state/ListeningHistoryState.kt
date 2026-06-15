package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class ListeningHistoryState(
    val status: Status,
    val button: String,
    val error: Error,
    val empty: Empty,
    val items: List<Item>,
) {
    @Immutable
    data class Item(
        val id: Long,
        val title: String,
        val date: String,
        val description: String,
    )

    @Immutable
    data class Error(
        val text: String,
        val button: String,
    )

    @Immutable
    data class Empty(
        val text: String,
        val title: String
    )
}