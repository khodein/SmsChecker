package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.mapper.SmtpEmailHostMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailHostBlock(
    private val smtpEmailHostMapper: SmtpEmailHostMapper,
) : BaseBlock<SmtpEmailHostState, Unit>() {

    private var hostState: String = ""
    private var hostErrorState: String? = null
    private var portState: String = ""
    private var portErrorState: String? = null
    private var isRequiredState: Boolean = false

    private val action = SmtpEmailHostAction(
        onChangeHost = ::onChangeHost,
        onChangePort = ::onChangePort
    )

    fun isRequired(): Boolean {
        hostErrorState = smtpEmailHostMapper.mapHostError(hostState)
        portErrorState = smtpEmailHostMapper.mapPortError(portState)
        isRequiredState = hostErrorState == null && portErrorState == null
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
                    error = this@SmtpEmailHostBlock.hostErrorState,
                ),
                portState = portState.copy(
                    value = this@SmtpEmailHostBlock.portState,
                    error = this@SmtpEmailHostBlock.portErrorState
                )
            )
        }
    }

    private fun onChangeHost(value: String) {
        this.hostState = value
        updateBlockState()
    }

    private fun onChangePort(value: String) {
        this.portState = value
        updateBlockState()
    }
}