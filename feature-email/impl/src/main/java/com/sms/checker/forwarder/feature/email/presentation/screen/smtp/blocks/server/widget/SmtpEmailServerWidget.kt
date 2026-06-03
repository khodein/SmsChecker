package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailServerWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailServerState,
    action: SmtpEmailServerAction,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FieldWidget(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                value = state.hostState.value,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                placeholder = state.hostState.placeholder,
                isError = state.hostState.error != null,
                error = state.hostState.error,
                onValueChange = action.onChangeHost
            )
            FieldWidget(
                modifier = Modifier
                    .width(75.dp)
                    .align(Alignment.Top),
                value = state.portState.value,
                maxLength = 3,
                textAlign = TextAlign.Center,
                placeholderAlign = TextAlign.Center,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                placeholder = state.portState.placeholder,
                isError = state.portState.error != null,
                onValueChange = action.onChangePort
            )
        }
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.userState.nameState.value,
            error = state.userState.nameState.error,
            placeholder = state.userState.nameState.placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
            isError = state.userState.nameState.error != null,
            onValueChange = action.onChangeUserName
        )
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.userState.passwordState.value,
            error = state.userState.passwordState.error,
            isError = state.userState.passwordState.error != null,
            placeholder = state.userState.passwordState.placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    modifier = Modifier.padding(end = SmsCheckerTheme.padding.extraSmall()),
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (passwordVisible) Icons.Default.Search else Icons.Default.Lock,
                        contentDescription = null,
                    )
                }
            },
            onValueChange = action.onChangePassword
        )
    }
}