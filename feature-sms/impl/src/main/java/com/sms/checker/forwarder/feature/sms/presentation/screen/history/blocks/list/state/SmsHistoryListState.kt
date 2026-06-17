package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.PageState

@Immutable
data class SmsHistoryListState(
    val error: Error,
    val pageState: PageState<ItemState>,
) {

    @Immutable
    data class Error(
        val text: String
    )

    @Immutable
    data class ItemState(
        val id: Long,
        val title: String,
        val time: String,
        val description: String,
    )
}