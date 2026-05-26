package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.mapper.SmtpEmailUserMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpEmailUserBlock(
    private val smtpEmailUserMapper: SmtpEmailUserMapper,
) : BaseBlock<SmtpEmailUserState, Unit>() {

    private var userNameState: String = ""
    private var passwordState: String = ""
    private var userNameErrorState: String? = null
    private var passwordErrorState: String? = null

    private val action = SmtpEmailUserAction(
        onChangeUserName = ::onChangeUserName,
        onChangePassword = ::onChangePassword,
    )

    fun isRequired(): Boolean {
        userNameErrorState = smtpEmailUserMapper.mapUserNameError(userNameState)
        passwordErrorState = smtpEmailUserMapper.mapPasswordError(passwordState)
        updateBlockState()
        return userNameErrorState == null && passwordErrorState == null
    }

    override fun getInitialUiState(): SmtpEmailUserState {
        return smtpEmailUserMapper.map(
            name = userNameState,
            password = passwordState,
            action = action,
        )
    }

    override fun updateBlockState() {
        setState {
            copy(
                userNameState = userNameState.copy(
                    value = this@SmtpEmailUserBlock.userNameState,
                    error = this@SmtpEmailUserBlock.userNameErrorState,
                ),
                passwordState = passwordState.copy(
                    value = this@SmtpEmailUserBlock.passwordState,
                    error = this@SmtpEmailUserBlock.passwordErrorState,
                ),
            )
        }
    }

    private fun onChangeUserName(value: String) {
        this.userNameState = value
        updateBlockState()
    }

    private fun onChangePassword(value: String) {
        this.passwordState = value
        updateBlockState()
    }
}