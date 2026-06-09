package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpTopBarAction(
    val onClickBackPressed: () -> Unit,
    val onChangeValue: (Boolean) -> Unit,
    val onClickDelete: () -> Unit,
)