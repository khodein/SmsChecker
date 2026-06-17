package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.mapper

import com.sms.checker.forwarder.feature.sms.R
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListState
import com.sms.checker.forwarder.framework.tools.res.ResProvider
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimePattern

internal class SmsHistoryListMapper(
    private val resProvider: ResProvider
) {

    fun mapList(
        list: List<SmsWithForwardsModel>
    ): List<SmsHistoryListState.ItemState> {
        return list.map(::mapItem)
    }

    private fun mapItem(
        model: SmsWithForwardsModel
    ): SmsHistoryListState.ItemState {
        return SmsHistoryListState.ItemState(
            id = model.sms.id ?: 0L,
            title = model.sms.sender,
            description = model.sms.body,
            time = model.sms.dateFormatter.format(LocalDateTimePattern.Long)
        )
    }

    fun mapError(): SmsHistoryListState.Error {
        return SmsHistoryListState.Error(
            text = resProvider.getString(R.string.feature_sms_history_loading_error)
        )
    }
}