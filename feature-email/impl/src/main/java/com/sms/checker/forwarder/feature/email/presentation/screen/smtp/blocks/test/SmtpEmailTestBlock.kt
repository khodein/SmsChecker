package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.usecase.SendSmtpMessageUseCase
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.mapper.SmtpEmailTestMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.block.BaseBlock
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SmtpEmailTestBlock(
    private val smtpEmailTestMapper: SmtpEmailTestMapper,
    private val sendSmtpMessageUseCase: SendSmtpMessageUseCase
) : BaseBlock<SmtpEmailTestState, SmtpEmailTestAction, SmtpEmailTestBlock.Provider>() {

    private var loadJob: Job? = null

    override val action = SmtpEmailTestAction(
        onClickReload = ::onClickReload
    )

    private var isRequiredState: Boolean = false

    private var statusState: SmtpEmailTestState.TestStatus = SmtpEmailTestState.TestStatus.None
        set(value) {
            isRequiredState = when (value) {
                SmtpEmailTestState.TestStatus.Succuss -> true
                else -> false
            }
            field = value
        }

    override fun getInitialUiState(): SmtpEmailTestState = SmtpEmailTestState(
        status = statusState,
        title = "",
        description = "",
        notice = "",
        buttonText = smtpEmailTestMapper.mapButton()
    )

    override fun updateBlockState() {
        setState {
            copy(
                status = statusState,
                title = smtpEmailTestMapper.mapTitle(statusState),
                description = smtpEmailTestMapper.mapDescription(statusState),
                notice = smtpEmailTestMapper.mapNotice(statusState)
            )
        }
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
            onEvent(SmtpEmailTestEvent.Scroll)
            statusState = SmtpEmailTestState.TestStatus.Loading
            updateBlockState()
            runCatching {
                sendSmtpMessageUseCase.invoke(
                    message = smtpEmailTestMapper.getTestMessage(),
                    model = model,
                )
            }.onSuccess {
                statusState = SmtpEmailTestState.TestStatus.Succuss
                onEvent(smtpEmailTestMapper.mapSuccessEvent())
                updateBlockState()
            }.onFailure {
                statusState = SmtpEmailTestState.TestStatus.Error
                onEvent(smtpEmailTestMapper.mapErrorEvent())
                updateBlockState()
            }
        }
    }

    fun isRequired(): Boolean {
        return isRequiredState
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