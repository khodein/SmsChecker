package com.sms.checker.forwarder.feature.email.presentation.route.smtp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class SmtpEmailKey(
    val id: Long?
) : NavKey