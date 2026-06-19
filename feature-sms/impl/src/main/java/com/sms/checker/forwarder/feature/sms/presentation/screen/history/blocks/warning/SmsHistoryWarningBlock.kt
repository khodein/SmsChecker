package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning

import com.sms.checker.forwarder.feature.listening.domain.usecase.ObserveListeningUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.ObserveLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.mapper.SmsHistoryWarningMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningState
import com.sms.checker.forwarder.feature.warning.domain.usecase.GetWarningUseCase
import com.sms.checker.forwarder.feature.warning.router.WarningRouter
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class SmsHistoryWarningBlock(
    private val observeListeningUseCase: ObserveListeningUseCase,
    private val observeLastSmsWithForwardsUseCase: ObserveLastSmsWithForwardsUseCase,
    private val getWarningUseCase: GetWarningUseCase,
    private val warningRouter: WarningRouter,
    private val mapper: SmsHistoryWarningMapper,
) : Block<SmsHistoryWarningState, SmsHistoryWarningAction, Unit>() {

    private var isListening: Boolean = false
    private var isHistoryNotEmpty: Boolean = false

    override val action = SmsHistoryWarningAction(
        onClick = ::onClick,
    )

    override fun getInitialUiState(): SmsHistoryWarningState = buildState()

    override fun updateBlockState() {
        setState { buildState() }
    }

    override fun startBlock() {
        super.startBlock()
        blockScope?.launch {
            observeListeningUseCase.invoke().collectLatest {
                isListening = it
                updateBlockState()
            }
        }
        blockScope?.launch {
            observeLastSmsWithForwardsUseCase.invoke(1).collectLatest { list ->
                isHistoryNotEmpty = list.isNotEmpty()
                updateBlockState()
            }
        }
    }

    private fun buildState(): SmsHistoryWarningState = mapper.map(
        isListening = isListening,
        isHistoryNotEmpty = isHistoryNotEmpty,
        warning = getWarningUseCase.invoke(),
    )

    private fun onClick() {
        warningRouter.gotoWarning()
    }
}
