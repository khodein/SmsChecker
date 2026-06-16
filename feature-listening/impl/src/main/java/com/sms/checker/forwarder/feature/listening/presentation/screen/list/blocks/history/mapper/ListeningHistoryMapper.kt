package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryState
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.framework.tools.res.ResProvider
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimePattern

internal class ListeningHistoryMapper(
    private val resProvider: ResProvider
) {
    fun mapItems(
        list: List<SmsWithForwardsModel>,
    ): List<ListeningHistoryState.Item> {
        return list.map {
            ListeningHistoryState.Item(
                id = it.sms.id ?: 0L,
                title = it.sms.sender,
                description = it.sms.body,
                date = it.sms.dateFormatter.format(LocalDateTimePattern.Long)
            )
        }
    }

    fun mapAll(): ListeningHistoryState.All {
        return ListeningHistoryState.All(
            text = resProvider.getString(R.string.feature_listening_show_all_history)
        )
    }

    fun mapEmpty(): ListeningHistoryState.Empty {
        return ListeningHistoryState.Empty(
            text = resProvider.getString(R.string.feature_listening_empty_history),
        )
    }

    fun mapTitle(): String {
        return resProvider.getString(R.string.feature_listening_history_title)
    }

    fun mapError(): ListeningHistoryState.Error {
        return ListeningHistoryState.Error(
            text = resProvider.getString(R.string.feature_listening_error_read_history),
            button = resProvider.getString(R.string.feature_listening_retry_loading)
        )
    }
}