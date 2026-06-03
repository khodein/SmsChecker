package com.sms.checker.forwarder.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sms.checker.forwarder.framework.router.NAV_TRANSITION_KEY
import com.sms.checker.forwarder.framework.router.NavTransition
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarWidget
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import kotlin.comparisons.then

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                SmsCheckerTheme {
                    SmsCheckerRoute()
                }
            }
        }
    }
}

@Composable
private fun SmsCheckerRoute() {
    val viewModel = koinViewModel<MainViewModel>()

    val snackbarHostState = SmsCheckerTheme.snackBarHostState
    val currentData = snackbarHostState.currentSnackbarData
    var lastData by remember { mutableStateOf<SnackbarData?>(null) }
    var snackbarAlignment by remember { mutableStateOf<Alignment>(Alignment.BottomCenter) }

    LaunchedEffect(currentData) {
        if (currentData != null) {
            lastData = currentData
            val visuals = currentData.visuals
            if (visuals is AppSnackBarVisuals) {
                snackbarAlignment = if (visuals.position == SnackBarPosition.Top) {
                    Alignment.TopCenter
                } else {
                    Alignment.BottomCenter
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SmsCheckerApp(
            viewModel = viewModel
        )

        AnimatedVisibility(
            visible = currentData != null,
            enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(animationSpec = tween(300)) { fullHeight ->
                    if (snackbarAlignment == Alignment.TopCenter) -fullHeight / 2 else fullHeight / 2
                },
            exit = fadeOut(animationSpec = tween(200)) +
                slideOutVertically(animationSpec = tween(200)) { fullHeight ->
                    if (snackbarAlignment == Alignment.TopCenter) -fullHeight / 2 else fullHeight / 2
                },
            modifier = Modifier
                .align(snackbarAlignment)
                .then(
                    if (snackbarAlignment == Alignment.TopCenter) Modifier.statusBarsPadding()
                    else Modifier.navigationBarsPadding()
                )
                .padding(horizontal = SmsCheckerTheme.padding.medium()),
        ) {
            val data = lastData
            val visuals = data?.visuals
            if (data != null && visuals is AppSnackBarVisuals) {
                SnackBarWidget(
                    text = visuals.message,
                    type = visuals.type,
                    actionLabel = visuals.actionLabel,
                    onAction = { data.performAction() },
                    onDismiss = if (visuals.withDismissAction) ({ data.dismiss() }) else null,
                    onSwipeDismiss = { data.dismiss() },
                )
            }
        }
    }
}

@Composable
private fun SmsCheckerApp(
    viewModel: MainViewModel,
) {
    NavDisplay(
        backStack = viewModel.getBackStack(),
        onBack = viewModel::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            val navTransition =
                targetState.entries.lastOrNull()?.metadata?.get(NAV_TRANSITION_KEY)
            when (navTransition) {
                NavTransition.NONE -> EnterTransition.None togetherWith ExitTransition.None
                NavTransition.SLIDE_VERTICAL -> slideInVertically { it } togetherWith fadeOut()
                NavTransition.SLIDE_HORIZONTAL -> slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                else -> fadeIn() togetherWith fadeOut()
            }
        },
        popTransitionSpec = {
            val navTransition =
                initialState.entries.lastOrNull()?.metadata?.get(NAV_TRANSITION_KEY)
            when (navTransition) {
                NavTransition.NONE -> EnterTransition.None togetherWith ExitTransition.None
                NavTransition.SLIDE_VERTICAL -> fadeIn() togetherWith slideOutVertically { it }
                NavTransition.SLIDE_HORIZONTAL -> slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                else -> fadeIn() togetherWith fadeOut()
            }
        },
        entryProvider = entryProvider {
            getKoin()
                .getAll<Router.Provider>()
                .forEach { it.invoke().invoke(this) }
        }
    )
}