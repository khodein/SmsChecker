package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar

import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.mapper.SmsHistoryTopBarMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarState
import com.sms.checker.forwarder.framework.block.Block
import com.sms.checker.forwarder.framework.router.Router

internal class SmsHistoryTopBarBlock(
    private val smsHistoryTopBarMapper: SmsHistoryTopBarMapper,
    private val router: Router,
) : Block<SmsHistoryTopBarState, SmsHistoryTopBarAction, Unit>() {

    override val action: SmsHistoryTopBarAction = SmsHistoryTopBarAction(
        onClickBack = router::goBack
    )

    override fun getInitialUiState(): SmsHistoryTopBarState {
        return buildState()
    }

    private fun buildState(): SmsHistoryTopBarState {
        return SmsHistoryTopBarState(
            title = smsHistoryTopBarMapper.mapTitle()
        )
    }

    override fun updateBlockState() = Unit
}