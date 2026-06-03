package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailNameWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailNameState,
    action: SmtpEmailNameAction,
) {
    FieldWidget(
        modifier = modifier.fillMaxWidth(),
        placeholder = state.placeholder,
        isError = state.error != null,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        error = state.error,
        value = state.value,
        onValueChange = action.onChangeValue
    )
}