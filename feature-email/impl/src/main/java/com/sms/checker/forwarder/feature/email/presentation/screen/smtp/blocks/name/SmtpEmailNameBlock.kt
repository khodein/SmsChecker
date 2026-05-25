package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.mapper.SmtpEmailNameMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailNameBlock(
    private val smtpEmailNameMapper: SmtpEmailNameMapper,
) : BaseBlock<SmtpEmailNameState, Unit>() {

    private var valueState: String = ""
    private var errorState: String? = null
    private var isRequiredState: Boolean = false

    private val action = SmtpEmailNameAction(
        onChangeValue = ::onChangeValue
    )

    override fun getInitialUiState(): SmtpEmailNameState {
        return smtpEmailNameMapper.map(
            value = valueState,
            action = action
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                value = valueState,
                error = errorState
            )
        }
    }

    fun isRequired(): Boolean {
        errorState = smtpEmailNameMapper.mapError(valueState)
        isRequiredState = errorState == null
        updateBlockState()
        return isRequiredState
    }

    private fun onChangeValue(value: String) {
        this.valueState = value
        updateBlockState()
    }
}