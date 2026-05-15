package com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen

import com.sms.checker.forwarder.feature.dev.DevRouter
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListState
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.mapper.DevListMapper
import com.sms.checker.forwarder.framework.BaseViewModel

internal class DevListViewModel(
    private val devListMapper: DevListMapper,
    private val router: DevRouter,
) : BaseViewModel<DevListState>() {

    val action = DevListAction(
        onClickDevListItem = { id ->
            when (id) {
                DevListMapper.PALETTE_ID -> router.gotoDevColorPalette()
            }
        }
    )

    override fun getInitialUiState(): DevListState {
        return devListMapper.getInitialState()
    }
}