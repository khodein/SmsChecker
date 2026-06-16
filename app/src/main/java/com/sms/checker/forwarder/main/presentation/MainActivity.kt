package com.sms.checker.forwarder.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sms.checker.forwarder.framework.BottomSheetSceneStrategy
import com.sms.checker.forwarder.framework.router.NAV_TRANSITION_KEY
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarWidget
import com.sms.checker.forwarder.router.RouterModule
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // условие проверяется каждый кадр; splash закрывается когда вернёт false
        splashScreen.setKeepOnScreenCondition { mainViewModel.isLoading.value }
        enableEdgeToEdge()
        setContent {
            SmsCheckerTheme {
                SmsCheckerRoute()
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
    var snackbarAlignment by remember { mutableStateOf(Alignment.BottomCenter) }
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

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
            bottomSheetStrategy = bottomSheetStrategy,
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
    bottomSheetStrategy: BottomSheetSceneStrategy<NavKey>,
    viewModel: MainViewModel,
) {
    NavDisplay(
        backStack = viewModel.getBackStack(),
        onBack = viewModel::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = listOf(bottomSheetStrategy),
        transitionSpec = {
            RouterModule.enterContentTransform(
                targetState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        popTransitionSpec = {
            RouterModule.popContentTransform(
                initialState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        predictivePopTransitionSpec = {
            RouterModule.popContentTransform(
                initialState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        entryProvider = entryProvider {
            getKoin()
                .getAll<Router.Provider>()
                .forEach { it.invoke().invoke(this) }
        }
    )
}