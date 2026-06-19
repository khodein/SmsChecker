package com.sms.checker.forwarder.feature.sms.presentation.screen.history

import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.SmsHistoryListBlock
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.SmsHistoryTopBarBlock
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.SmsHistoryWarningBlock
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.mapper.SmsHistoryMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.state.SmsHistoryAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.state.SmsHistoryState
import com.sms.checker.forwarder.framework.BaseViewModel

internal class SmsHistoryViewModel(
    private val mapper: SmsHistoryMapper,
    private val smsHistoryListBlock: SmsHistoryListBlock,
    private val smsHistoryTopBarBlock: SmsHistoryTopBarBlock,
    private val smsHistoryWarningBlock: SmsHistoryWarningBlock,
) : BaseViewModel<SmsHistoryState, SmsHistoryAction>() {

    override val action: SmsHistoryAction = SmsHistoryAction(
        listAction = smsHistoryListBlock.action,
        topBarAction = smsHistoryTopBarBlock.action,
        warningAction = smsHistoryWarningBlock.action,
    )

    init {
        attach()
    }

    override fun attach() {
        registerBlocks {
            add(smsHistoryListBlock)
            add(smsHistoryTopBarBlock)
            add(smsHistoryWarningBlock)
        }
    }

    override fun updateViewState() {
        setState { buildState() }
    }

    override fun getInitialUiState(): SmsHistoryState {
        return buildState()
    }

    private fun buildState(): SmsHistoryState {
        return SmsHistoryState(
            listState = smsHistoryListBlock.blockState.value,
            topBarState = smsHistoryTopBarBlock.blockState.value,
            warningState = smsHistoryWarningBlock.blockState.value,
        )
    }
}
