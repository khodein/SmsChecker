package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.state.SmtpEmailHostState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailHostWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailHostState,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldWidget(
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            value = state.hostState.value,
            placeholder = state.hostState.placeholder,
            isError = state.hostState.error != null,
            error = state.hostState.error,
            onValueChange = state.action.onChangeHost
        )
        FieldWidget(
            modifier = Modifier.width(75.dp).align(Alignment.CenterVertically),
            value = state.portState.value,
            maxLength = 3,
            textAlign = TextAlign.Center,
            placeholderAlign = TextAlign.Center,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            placeholder = state.portState.placeholder,
            isError = state.portState.error != null,
            onValueChange = state.action.onChangePort
        )
    }
}