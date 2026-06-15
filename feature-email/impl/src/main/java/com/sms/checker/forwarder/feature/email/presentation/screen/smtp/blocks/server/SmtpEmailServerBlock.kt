package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.mapper.SmtpEmailServerMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerState
import com.sms.checker.forwarder.framework.block.Block

internal class SmtpEmailServerBlock(
    private val smtpEmailServerMapper: SmtpEmailServerMapper,
) : Block<SmtpEmailServerState, SmtpEmailServerAction, Unit>() {

    private var hostState: String = ""
    private var portState: String = ""
    private var hostErrorState: String? = null

    private var userNameState: String = ""
    private var passwordState: String = ""
    private var userNameErrorState: String? = null
    private var passwordErrorState: String? = null

    override val action = SmtpEmailServerAction(
        onChangeHost = ::onChangeHost,
        onChangePort = ::onChangePort,
        onChangeUserName = ::onChangeUserName,
        onChangePassword = ::onChangePassword,
    )

    val isRequiredState: Boolean
        get() {
            hostErrorState = smtpEmailServerMapper.mapHostError(
                hostValue = hostState,
                portValue = portState,
            )
            userNameErrorState = smtpEmailServerMapper.mapNameError(userNameState)
            passwordErrorState = smtpEmailServerMapper.mapPasswordError(passwordState)
            updateBlockState()
            return hostErrorState == null && userNameErrorState == null && passwordErrorState == null
        }

    override fun getInitialUiState(): SmtpEmailServerState {
        return smtpEmailServerMapper.map(
            hostValue = hostState,
            portValue = portState,
            name = userNameState,
            password = passwordState,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                hostState = hostState.copy(
                    value = this@SmtpEmailServerBlock.hostState,
                    error = this@SmtpEmailServerBlock.hostErrorState,
                ),
                portState = portState.copy(
                    value = this@SmtpEmailServerBlock.portState,
                    error = this@SmtpEmailServerBlock.hostErrorState,
                ),
                userState = userState.copy(
                    nameState = userState.nameState.copy(
                        value = this@SmtpEmailServerBlock.userNameState,
                        error = this@SmtpEmailServerBlock.userNameErrorState,
                    ),
                    passwordState = userState.passwordState.copy(
                        value = this@SmtpEmailServerBlock.passwordState,
                        error = this@SmtpEmailServerBlock.passwordErrorState,
                    ),
                ),
            )
        }
    }

    fun onChangeHost(value: String) {
        this.hostState = value
        updateBlockState()
    }

    fun onChangePort(value: String) {
        this.portState = value.filter { it.isDigit() }
        updateBlockState()
    }

    fun onChangeUserName(value: String) {
        this.userNameState = value
        updateBlockState()
    }

    fun onChangePassword(value: String) {
        this.passwordState = value
        updateBlockState()
    }
}