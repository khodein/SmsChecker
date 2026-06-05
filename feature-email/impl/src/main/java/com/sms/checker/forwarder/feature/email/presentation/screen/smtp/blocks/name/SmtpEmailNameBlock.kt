package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.mapper.SmtpEmailNameMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailNameBlock(
    private val smtpEmailNameMapper: SmtpEmailNameMapper,
) : BaseBlock<SmtpEmailNameState, SmtpEmailNameAction, Unit>() {

    private var valueState: String = ""
    private var errorState: String? = null

    val isRequiredState: Boolean
        get() {
            errorState = smtpEmailNameMapper.mapError(valueState)
            updateBlockState()
            return errorState == null
        }

    override val action = SmtpEmailNameAction(
        onChangeValue = ::onChangeValue
    )

    override fun getInitialUiState(): SmtpEmailNameState {
        return buildState()
    }

    override fun updateBlockState() {
        setState {
            buildState()
        }
    }

    private fun buildState(): SmtpEmailNameState {
        return  smtpEmailNameMapper.map(
            value = valueState,
            error = errorState
        )
    }

    fun onChangeValue(value: String) {
        this.valueState = value
        updateBlockState()
    }
}