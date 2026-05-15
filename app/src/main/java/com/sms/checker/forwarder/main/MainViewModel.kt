package com.sms.checker.forwarder.main

import androidx.lifecycle.ViewModel
import com.sms.checker.forwarder.feature.dev.DevRouter
import com.sms.checker.forwarder.router.Router

class MainViewModel(
    private val devRouter: DevRouter,
    private val router: Router,
) : ViewModel() {

    init {
        setStartAppRouting()
    }

    private fun setStartAppRouting() {
        if (router.getBackStack().isEmpty()) {
            devRouter.gotoDevList()
        }
    }

    fun getBackStack() = router.getBackStack()
    fun goBack() = router.goBack()
}