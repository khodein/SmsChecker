package com.sms.checker.forwarder.feature.sms.domain.model.exception

sealed class SmsException(message: String) : Exception(message) {
    class SmsExceptionNotFound(id: Long) : SmsException("Sms not found: id=$id")
    class SmsExceptionNotUpdated(id: Long) : SmsException("Sms not updated: id=$id")
}