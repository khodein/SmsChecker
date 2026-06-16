package com.sms.checker.forwarder.feature.listening.presentation.route

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.ListeningListScreen
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.ListeningListViewModel
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.widget.onEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.widget.onEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.widget.onEvent
import com.sms.checker.forwarder.framework.OnLifecycle
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun ListeningListRoute(
    viewModel: ListeningListViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    val context = LocalContext.current
    val snackbarHostState = SmsCheckerTheme.snackBarHostState
    val lazyListState = rememberLazyListState()
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        action.listeningAction.onPermissionListeningResult.invoke(permissions.values.all { it })
    }

    LaunchedEffect(state.listeningState.needPermissionState) {
        if (state.listeningState.needPermissionState == null) return@LaunchedEffect
        val required = buildList {
            add(Manifest.permission.RECEIVE_SMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        val allGranted = required.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            action.listeningAction.onPermissionListeningResult.invoke(true)
        } else {
            permissionLauncher.launch(required.toTypedArray())
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ListeningConfigEvent -> event.onEvent(snackbarHostState)
                is ListeningHistoryEvent -> event.onEvent(snackbarHostState)
                is ListeningEvent -> event.onEvent(
                    snackbarHostState = snackbarHostState,
                    lazyListState = lazyListState
                )
            }
        }
    }

    OnLifecycle(
        onStart = viewModel::onUiStart,
        onStop = viewModel::onUiStop
    )

    ListeningListScreen(
        state = state,
        lazyListState = lazyListState,
        action = action,
    )
}
