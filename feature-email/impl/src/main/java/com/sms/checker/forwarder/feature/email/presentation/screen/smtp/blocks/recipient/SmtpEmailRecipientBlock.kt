package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.mapper.SmtpEmailRecipientMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientState
import com.sms.checker.forwarder.framework.block.Block

internal class SmtpEmailRecipientBlock(
    private val smtpEmailRecipientMapper: SmtpEmailRecipientMapper,
) : Block<SmtpEmailRecipientState, SmtpEmailRecipientAction, Unit>() {

    private var emailState: String = ""
    private var subjectState: String = ""
    private var emailErrorState: String? = null
    private var subjectErrorState: String? = null

    val isRequiredState: Boolean
        get() {
            emailErrorState = smtpEmailRecipientMapper.mapEmailError(emailState)
            subjectErrorState = smtpEmailRecipientMapper.mapSubjectError(subjectState)
            updateBlockState()
            return emailErrorState == null && subjectErrorState == null
        }

    override val action = SmtpEmailRecipientAction(
        onChangeEmail = ::onChangeEmail,
        onChangeSubject = ::onChangeSubject,
    )

    override fun getInitialUiState(): SmtpEmailRecipientState {
        return smtpEmailRecipientMapper.map(
            email = emailState,
            subject = subjectState,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                emailState = emailState.copy(
                    value = this@SmtpEmailRecipientBlock.emailState,
                    error = this@SmtpEmailRecipientBlock.emailErrorState,
                ),
                subjectState = subjectState.copy(
                    value = this@SmtpEmailRecipientBlock.subjectState,
                    error = this@SmtpEmailRecipientBlock.subjectErrorState,
                ),
            )
        }
    }

    fun onChangeEmail(value: String) {
        this.emailState = value
        updateBlockState()
    }

    fun onChangeSubject(value: String) {
        this.subjectState = value
        updateBlockState()
    }
}
