package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper.SmtpTopBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.block.BaseBlock
import com.sms.checker.forwarder.framework.router.Router

internal class SmtpTopBarBlock(
    private val smtpTopBarMapper: SmtpTopBarMapper,
    private val router: Router,
) : BaseBlock<SmtpTopBarState, SmtpTopBarAction, SmtpTopBarBlock.Provider>() {

    var status: SmtpEmailStatus? = null
        private set

    override val action = SmtpTopBarAction(
        onClickBackPressed = ::onClickBackPressed,
        onChangeValue = ::onChangeValue,
    )

    override fun getInitialUiState(): SmtpTopBarState = buildState()

    override fun updateBlockState() {
        setState { buildState() }
    }

    fun onChangeValue(status: SmtpEmailStatus?) {
        this.status = status
        updateBlockState()
    }

    private fun buildState() = smtpTopBarMapper.map(status = status)

    private fun onClickBackPressed() {
        router.goBack()
    }

    private fun onChangeValue(isEnabled: Boolean) {
        val status = if (isEnabled) {
            SmtpEmailStatus.Enable
        } else {
            SmtpEmailStatus.Disable
        }
        this@SmtpTopBarBlock.onChangeValue(status)
    }

    interface Provider {
        fun onChangeStatus()
    }
}