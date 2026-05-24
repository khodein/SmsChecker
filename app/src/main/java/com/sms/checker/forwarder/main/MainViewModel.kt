package com.sms.checker.forwarder.main

import androidx.lifecycle.ViewModel
import com.sms.checker.forwarder.feature.listening.router.ListeningRouter
import com.sms.checker.forwarder.router.Router

class MainViewModel(
    private val listeningRouter: ListeningRouter,
    private val router: Router,
) : ViewModel() {

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