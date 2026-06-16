package com.sms.checker.forwarder.feature.listening.presentation.dialog

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class ListeningWarningKey(
    val title: String,
    val description: String,
) : NavKey {
}