package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.mapper.SmtpEmailRecipientMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailRecipientBlock(
    private val smtpEmailRecipientMapper: SmtpEmailRecipientMapper,
) : BaseBlock<SmtpEmailRecipientState, Unit>() {

    private var emailState: String = ""
    private var nameState: String = ""
    private var emailErrorState: String? = null
    private var nameErrorState: String? = null

    private val action = SmtpEmailRecipientAction(
        onChangeEmail = ::onChangeEmail,
        onChangeName = ::onChangeName,
    )

    fun isRequired(): Boolean {
        emailErrorState = smtpEmailRecipientMapper.mapEmailError(emailState)
        nameErrorState = smtpEmailRecipientMapper.mapNameError(nameState)
        updateBlockState()
        return emailErrorState == null && nameErrorState == null
    }

    override fun getInitialUiState(): SmtpEmailRecipientState {
        return smtpEmailRecipientMapper.map(
            email = emailState,
            name = nameState,
            action = action,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                emailState = emailState.copy(
                    value = this@SmtpEmailRecipientBlock.emailState,
                    error = this@SmtpEmailRecipientBlock.emailErrorState,
                ),
                nameState = nameState.copy(
                    value = this@SmtpEmailRecipientBlock.nameState,
                    error = this@SmtpEmailRecipientBlock.nameErrorState,
                ),
            )
        }
    }

    private fun onChangeEmail(value: String) {
        this.emailState = value
        updateBlockState()
    }

    private fun onChangeName(value: String) {
        this.nameState = value
        updateBlockState()
    }
}
