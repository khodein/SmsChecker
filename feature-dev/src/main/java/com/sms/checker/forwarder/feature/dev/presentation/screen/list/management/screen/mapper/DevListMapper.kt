package com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.mapper

import com.sms.checker.forwarder.feature.dev.R
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListItemState
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListState
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.tools.res.ResProvider

internal class DevListMapper(
    private val resProvider: ResProvider,
) {
    fun getInitialState(): DevListState {
        return DevListState(
            status = Status.SUCCESS,
            items = listOf(
                getPaletteDevListItemState()
            )
        )
    }

    private fun getPaletteDevListItemState(): DevListItemState {
        return DevListItemState(
            id = PALETTE_ID,
            name = resProvider.getString(R.string.feature_dev_item_color_palette)
        )
    }

    companion object {
        const val PALETTE_ID = "palette_dev_list_item_id"
    }
}