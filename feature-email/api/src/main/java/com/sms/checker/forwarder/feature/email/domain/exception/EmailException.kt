package com.sms.checker.forwarder.feature.email.domain.exception

sealed class EmailException(message: String) : Exception(message) {
    class SmtpConfigNotFound(id: Long) : EmailException("Smtp config not found: id=$id")
    class SmtpConfigNotUpdated(id: Long) : EmailException("Smtp config not updated: id=$id")
}