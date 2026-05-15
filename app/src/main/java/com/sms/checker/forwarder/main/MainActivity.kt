package com.sms.checker.forwarder.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.router.NAV_TRANSITION_KEY
import com.sms.checker.forwarder.router.NavTransition
import com.sms.checker.forwarder.router.Router
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

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
    SmsCheckerApp(
        viewModel = viewModel
    )
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
            val navTransition = targetState.entries.lastOrNull()?.metadata?.get(NAV_TRANSITION_KEY)
            when (navTransition) {
                NavTransition.NONE -> EnterTransition.None togetherWith ExitTransition.None
                NavTransition.SLIDE_VERTICAL -> slideInVertically { it } togetherWith fadeOut()
                NavTransition.SLIDE_HORIZONTAL -> slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                else ->  fadeIn() togetherWith fadeOut()
            }
        },
        popTransitionSpec = {
            val navTransition = initialState.entries.lastOrNull()?.metadata?.get(NAV_TRANSITION_KEY)
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