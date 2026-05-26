package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.mapper.SmtpEmailHostMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailHostBlock(
    private val smtpEmailHostMapper: SmtpEmailHostMapper,
) : BaseBlock<SmtpEmailHostState, Unit>() {

    private var hostState: String = ""
    private var portState: String = ""
    private var errorState: String? = null
    private var isRequiredState: Boolean = false

    private val action = SmtpEmailHostAction(
        onChangeHost = ::onChangeHost,
        onChangePort = ::onChangePort
    )

    fun isRequired(): Boolean {
        errorState = smtpEmailHostMapper.mapHostError(
            hostValue = hostState,
            portValue = portState
        )
        isRequiredState = errorState == null
        updateBlockState()
        return isRequiredState
    }

    override fun getInitialUiState(): SmtpEmailHostState {
        return smtpEmailHostMapper.map(
            action = action,
            hostValue = hostState,
            portValue = portState,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                hostState = hostState.copy(
                    value = this@SmtpEmailHostBlock.hostState,
                    error = this@SmtpEmailHostBlock.errorState,
                ),
                portState = portState.copy(
                    value = this@SmtpEmailHostBlock.portState,
                    error = this@SmtpEmailHostBlock.errorState
                )
            )
        }
    }

    private fun onChangeHost(value: String) {
        this.hostState = value
        updateBlockState()
    }

    private fun onChangePort(value: String) {
        val validatePort = value.filter { it.isDigit() }
        this.portState = validatePort
        updateBlockState()
    }
}