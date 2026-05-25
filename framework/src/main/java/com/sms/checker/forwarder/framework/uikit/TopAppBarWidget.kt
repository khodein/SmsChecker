package com.sms.checker.forwarder.framework.uikit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWidget(
    modifier: Modifier = Modifier,
    title: String,
    onClickBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = SmsCheckerTheme.typography.titleLarge,
                color = SmsCheckerTheme.color.onBackground
            )
        },
        navigationIcon = {
            NavigationBackWidget(
                onClick = onClickBackPressed
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SmsCheckerTheme.color.surface
        )
    )
}