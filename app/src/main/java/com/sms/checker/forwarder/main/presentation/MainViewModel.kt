package com.sms.checker.forwarder.main.presentation

import androidx.lifecycle.ViewModel
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.router.ListeningRouter
import com.sms.checker.forwarder.framework.router.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val listeningRouter: ListeningRouter,
    private val emailRouter: EmailRouter,
    private val router: Router,
) : ViewModel() {

    // Splash screen держится пока true; переключи в true перед загрузкой, в false — когда готово
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        setStartAppRouting()
    }

    private fun setStartAppRouting() {
        if (router.getBackStack().isEmpty()) {
            listeningRouter.gotoListening()
        }
    }

    fun getBackStack() = router.getBackStack()
    fun goBack() = router.goBack()
}