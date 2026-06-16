package com.sms.checker.forwarder.framework.uikit

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWidget(
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = true,
    dragHandle: @Composable (() -> Unit)? = {
        BottomSheetDefaults.DragHandle(
            color = SmsCheckerTheme.color.outline
        )
    },
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        shape = SmsCheckerTheme.corner.top(SmsCheckerTheme.corner.large()),
        containerColor = SmsCheckerTheme.color.surface,
        contentColor = SmsCheckerTheme.color.onSurface,
        tonalElevation = 0.dp,
        scrimColor = SmsCheckerTheme.color.onBackground.copy(alpha = 0.32f),
        dragHandle = dragHandle,
        content = content,
    )
}
