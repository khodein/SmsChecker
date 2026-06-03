package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar

import androidx.room.Update
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper.SmtpBottomBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailEvent
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpBottomBarBlock(
    private val smtpBottomBarMapper: SmtpBottomBarMapper,
) : BaseBlock<SmtpBottomBarState, SmtpBottomBarAction, SmtpBottomBarBlock.Provider>() {

    private var isUpdate: Boolean = false

    override val action = SmtpBottomBarAction(
        onClickAdd = ::onClickAdd
    )

    override fun getInitialUiState(): SmtpBottomBarState {
        return smtpBottomBarMapper.map()
    }

    override fun updateBlockState() {

    }

    private fun onClickAdd() {
        val requiredMap = blockProvider?.getRequired() ?: return
        val requiredWithoutTest = requiredMap.filter { it.key != Required.Test }
        val isRequired = requiredWithoutTest.any { it.value }
        val isTestRequired = requiredMap[Required.Test] ?: false
        if (!isRequired) {
            onEvent(smtpBottomBarMapper.getErrorEvent())
            return
        }

        if (isTestRequired) {
            if (isUpdate) {
                blockProvider?.onUpdate()
            } else {
                blockProvider?.onSave()
            }
            return
        }

        blockProvider?.onRequired()
    }

    fun setIsUpdate(isUpdate: Boolean) {
        this.isUpdate = isUpdate
    }

    interface Provider {
        fun getRequired(): Map<Required, Boolean>
        fun onRequired()
        fun onSave()
        fun onUpdate()
    }

    enum class Required {
        Name,
        Server,
        From,
        Recipient,
        Test,
    }
}