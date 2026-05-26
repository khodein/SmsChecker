package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.state.SmtpEmailUserState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.FieldWidget

@Composable
internal fun SmtpEmailUserWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailUserState,
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
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.userNameState.value,
            error = state.userNameState.error,
            placeholder = state.userNameState.placeholder,
            isError = state.userNameState.error != null,
            onValueChange = state.action.onChangeUserName
        )
        FieldWidget(
            modifier = Modifier.fillMaxWidth(),
            value = state.passwordState.value,
            error = state.passwordState.error,
            isError = state.passwordState.error != null,
            placeholder = state.passwordState.placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
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
            onValueChange = state.action.onChangePassword
        )
    }
}