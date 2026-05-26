package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailItemState

internal class SmtpEmailMapper {

    fun mapSmtpEmailItems(
        smtpEmailNameState: SmtpEmailNameState,
        smtpEmailHostState: SmtpEmailHostState,
        smtpEmailUserState: SmtpEmailUserState,
    ): List<SmtpEmailItemState> {
        return buildList {
            add(getSmtpEmailNameItemState(smtpEmailNameState))
            add(getSmtpEmailHostItemState(smtpEmailHostState))
            add(getSmtpEmailUserItemState(smtpEmailUserState))
        }
    }

    private fun getSmtpEmailUserItemState(
        state: SmtpEmailUserState
    ): SmtpEmailItemState.UserState {
        return SmtpEmailItemState.UserState(
            id = "user_state_id",
            contentType = "user_state_content_type",
            state = state,
        )
    }

    private fun getSmtpEmailNameItemState(
        state: SmtpEmailNameState,
    ): SmtpEmailItemState.NameState {
        return SmtpEmailItemState.NameState(
            id = "name_state_id",
            contentType = "name_state_content_type",
            state = state,
        )
    }

    private fun getSmtpEmailHostItemState(
        state: SmtpEmailHostState
    ): SmtpEmailItemState.HostState {
        return SmtpEmailItemState.HostState(
            id = "host_state_id",
            contentType = "host_state_content_type",
            state = state
        )
    }
}