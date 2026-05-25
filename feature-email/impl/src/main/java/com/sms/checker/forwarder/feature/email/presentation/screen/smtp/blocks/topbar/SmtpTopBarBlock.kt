package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper.SmtpTopBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.block.BaseBlock
import com.sms.checker.forwarder.framework.router.Router

internal class SmtpTopBarBlock(
    private val smtpTopBarMapper: SmtpTopBarMapper,
    private val router: Router,
) : BaseBlock<SmtpTopBarState, Unit>() {

    private val action = SmtpTopBarAction(
        onClickBackPressed = ::onClickBackPressed,
        onClickNotice = ::onClickNotice
    )

    override fun getInitialUiState(): SmtpTopBarState {
        return smtpTopBarMapper.map(
            action = action
        )
    }

    override fun updateBlockState() {

    }

    private fun onClickBackPressed() {
        router.goBack()
    }

    private fun onClickNotice() {

    }
}