package com.sms.checker.forwarder.feature.sms.domain.model

import com.sms.checker.forwarder.framework.tools.time.LocalDateTimeFormatter
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimePattern

data class SmsModel(
    val id: Long? = null,
    val sender: String,
    val body: String,
    val dateFormatter: LocalDateTimeFormatter,
) {
    val message: String
        get() = buildString {
            append(sender)
            appendLine()
            append(body)
            appendLine()
            append(dateFormatter.format(LocalDateTimePattern.Medium))
        }
}