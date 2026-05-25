package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailItemState

internal class SmtpEmailMapper {

    fun mapSmtpEmailItems(
        smtpEmailNameState: SmtpEmailNameState,
    ): List<SmtpEmailItemState> {
        return buildList {
            add(getSmtpEmailNameItemState(smtpEmailNameState))
        }
    }

    private fun getSmtpEmailNameItemState(
        smtpEmailNameState: SmtpEmailNameState,
    ): SmtpEmailItemState.NameState {
        return SmtpEmailItemState.NameState(
            id = "name_state_id",
            contentType = "name_state_content_type",
            state = smtpEmailNameState,
        )
    }
}