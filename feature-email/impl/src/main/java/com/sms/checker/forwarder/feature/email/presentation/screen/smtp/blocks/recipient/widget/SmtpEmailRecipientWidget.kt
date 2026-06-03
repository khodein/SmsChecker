package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailRecipientWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailRecipientState,
    action: SmtpEmailRecipientAction,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = SmsCheckerTheme.padding.extraSmall(),
                    start = SmsCheckerTheme.padding.small()
                ),
            text = state.title,
            style = SmsCheckerTheme.typography.titleMedium,
            color = SmsCheckerTheme.color.onSurface,
        )
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.emailState.value,
            error = state.emailState.error,
            placeholder = state.emailState.placeholder,
            isError = state.emailState.error != null,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            onValueChange = action.onChangeEmail,
        )
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.subjectState.value,
            error = state.subjectState.error,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
            placeholder = state.subjectState.placeholder,
            isError = state.subjectState.error != null,
            onValueChange = action.onChangeSubject,
        )
    }
}
