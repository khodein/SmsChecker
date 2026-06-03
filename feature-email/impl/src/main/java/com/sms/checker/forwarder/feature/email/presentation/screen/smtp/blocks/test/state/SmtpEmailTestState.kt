package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class SmtpEmailTestState(
    val status: TestStatus,
    val title: String,
    val description: String,
    val notice: String,
    val buttonText: String
) {
    enum class TestStatus {
        None,
        Loading,
        Succuss,
        Error,
    }
}