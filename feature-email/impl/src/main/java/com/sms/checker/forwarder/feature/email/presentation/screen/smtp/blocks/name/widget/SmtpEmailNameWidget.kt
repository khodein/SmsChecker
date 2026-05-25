package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailNameWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailNameState,
) {
    FieldWidget(
        modifier = modifier.fillMaxWidth(),
        placeholder = state.placeholder,
        isError = state.error != null,
        error = state.error,
        value = state.value,
        onValueChange = state.action.onChangeValue
    )
}