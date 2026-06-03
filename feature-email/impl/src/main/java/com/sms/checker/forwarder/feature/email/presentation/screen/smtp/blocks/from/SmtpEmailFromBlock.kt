package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.mapper.SmtpEmailFromMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state.SmtpEmailFromAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state.SmtpEmailFromState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailFromBlock(
    private val smtpEmailFromMapper: SmtpEmailFromMapper,
) : BaseBlock<SmtpEmailFromState, SmtpEmailFromAction, Unit>() {

    private var emailState: String = ""
    private var nameState: String = ""
    private var emailErrorState: String? = null
    private var nameErrorState: String? = null

    override val action = SmtpEmailFromAction(
        onChangeEmail = ::onChangeEmail,
        onChangeName = ::onChangeName,
    )

    fun isRequired(): Boolean {
        emailErrorState = smtpEmailFromMapper.mapEmailError(emailState)
        nameErrorState = smtpEmailFromMapper.mapNameError(nameState)
        updateBlockState()
        return emailErrorState == null && nameErrorState == null
    }

    override fun getInitialUiState(): SmtpEmailFromState {
        return smtpEmailFromMapper.map(
            email = emailState,
            name = nameState,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                emailState = emailState.copy(
                    value = this@SmtpEmailFromBlock.emailState,
                    error = this@SmtpEmailFromBlock.emailErrorState,
                ),
                nameState = nameState.copy(
                    value = this@SmtpEmailFromBlock.nameState,
                    error = this@SmtpEmailFromBlock.nameErrorState,
                ),
            )
        }
    }

    fun onChangeEmail(value: String) {
        emailState = value
        updateBlockState()
    }

    fun onChangeName(value: String) {
        nameState = value
        updateBlockState()
    }
}