package com.sms.checker.forwarder.feature.warning

import com.sms.checker.forwarder.feature.warning.data.WarningRepositoryImpl
import com.sms.checker.forwarder.feature.warning.domain.WarningRepository
import com.sms.checker.forwarder.feature.warning.domain.usecase.GetWarningUseCase
import com.sms.checker.forwarder.feature.warning.domain.usecase.GetWarningUseCaseImpl
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.WarningViewModel
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.mapper.WarningScreenMapper
import com.sms.checker.forwarder.feature.warning.router.WarningProviderImpl
import com.sms.checker.forwarder.feature.warning.router.WarningRouter
import com.sms.checker.forwarder.feature.warning.router.WarningRouterImpl
import com.sms.checker.forwarder.feature.warning.widget.WarningUi
import com.sms.checker.forwarder.feature.warning.widget.WarningUiImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object WarningModule {

    fun get() = module {
        // presentation
        viewModelOf(::WarningViewModel)
        singleOf(::WarningScreenMapper)

        // domain
        singleOf(::GetWarningUseCaseImpl) bind GetWarningUseCase::class

        // data
        singleOf(::WarningRepositoryImpl) bind WarningRepository::class

        // navigation
        singleOf(::WarningRouterImpl) bind WarningRouter::class
        singleOf(::WarningProviderImpl) bind Router.Provider::class

        // widget
        singleOf(::WarningUiImpl) bind WarningUi::class
    }
}
