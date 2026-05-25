package com.sms.checker.forwarder.main.presentation

import androidx.lifecycle.ViewModel
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.router.ListeningRouter
import com.sms.checker.forwarder.framework.router.Router

class MainViewModel(
    private val listeningRouter: ListeningRouter,
    private val emailRouter: EmailRouter,
    private val router: Router,
) : ViewModel() {

    init {
        setStartAppRouting()
    }

    private fun setStartAppRouting() {
        if (router.getBackStack().isEmpty()) {
            listeningRouter.gotoListening()
//            emailRouter.gotoSmtpEmail()
        }
    }

    fun getBackStack() = router.getBackStack()
    fun goBack() = router.goBack()
}