package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.usecase.SendSmtpMessageUseCase
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.mapper.SmtpEmailTestMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SmtpEmailTestBlock(
    private val smtpEmailTestMapper: SmtpEmailTestMapper,
    private val sendSmtpMessageUseCase: SendSmtpMessageUseCase
) : Block<SmtpEmailTestState, SmtpEmailTestAction, SmtpEmailTestBlock.Provider>() {

    private var loadJob: Job? = null

    override val action = SmtpEmailTestAction(
        onClickReload = ::onClickReload
    )

    var isRequiredState: Boolean = false
        private set

    private var statusState: SmtpEmailTestState.TestStatus = SmtpEmailTestState.TestStatus.None
        set(value) {
            isRequiredState = when (value) {
                SmtpEmailTestState.TestStatus.Succuss -> true
                else -> false
            }
            field = value
        }

    override fun getInitialUiState(): SmtpEmailTestState = buildState()

    override fun updateBlockState() {
        setState { buildState() }
    }

    fun onTest(model: SmtpEmailModel) {
        if (statusState == SmtpEmailTestState.TestStatus.Loading) {
            return
        }
        load(model)
    }

    private fun load(model: SmtpEmailModel) {
        loadJob?.cancel()
        loadJob = blockScope?.launch {
            updateLoading()
            runCatching {
                sendSmtpMessageUseCase.invoke(
                    message = smtpEmailTestMapper.getTestMessage(),
                    model = model,
                )
            }.onSuccess {
                updateSuccess()
            }.onFailure {
                updateError()
            }
        }
    }

    private fun buildState(): SmtpEmailTestState {
        return SmtpEmailTestState(
            status = statusState,
            title = smtpEmailTestMapper.mapTitle(statusState),
            description = smtpEmailTestMapper.mapDescription(statusState),
            notice = smtpEmailTestMapper.mapNotice(statusState),
            buttonText = smtpEmailTestMapper.mapButton()
        )
    }

    private fun updateLoading() {
        onEvent(SmtpEmailTestEvent.Scroll)
        statusState = SmtpEmailTestState.TestStatus.Loading
        updateBlockState()
    }

    private fun updateSuccess() {
        statusState = SmtpEmailTestState.TestStatus.Succuss
        onEvent(smtpEmailTestMapper.mapSuccessEvent())
        updateBlockState()
    }

    private fun updateError() {
        statusState = SmtpEmailTestState.TestStatus.Error
        onEvent(smtpEmailTestMapper.mapErrorEvent())
        updateBlockState()
    }

    private fun onClickReload() {
        statusState = SmtpEmailTestState.TestStatus.None
        updateBlockState()
        blockProvider?.onReload()
    }

    interface Provider {
        fun onReload()
    }
}