package com.sms.checker.forwarder.feature.email

import com.sms.checker.forwarder.feature.email.data.EmailRepositoryImpl
import com.sms.checker.forwarder.feature.email.data.mapper.SmtpEmailDataMapper
import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpEmailByForwardingSourceUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpEmailByForwardingSourceUseCaseImpl
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper.SmtpBottomBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.SmtpEmailHostBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.mapper.SmtpEmailHostMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.SmtpEmailNameBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.mapper.SmtpEmailNameMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper.SmtpTopBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper.SmtpEmailMapper
import com.sms.checker.forwarder.feature.email.router.EmailProviderImpl
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.email.router.EmailRouterImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object EmailModule {

    fun get() = module {
        // data
        singleOf(::EmailRepositoryImpl) bind EmailRepository::class
        singleOf(::SmtpEmailDataMapper)

        // domain
        factoryOf(::GetSmtpEmailByForwardingSourceUseCaseImpl) bind GetSmtpEmailByForwardingSourceUseCase::class

        // navigation
        singleOf(::EmailRouterImpl) bind EmailRouter::class
        singleOf(::EmailProviderImpl) bind Router.Provider::class

        // presentation
        viewModelOf(::SmtpEmailViewModel)
        factoryOf(::SmtpBottomBarBlock)
        factoryOf(::SmtpTopBarBlock)
        factoryOf(::SmtpEmailNameBlock)
        factoryOf(::SmtpEmailHostBlock)

        // mappers
        singleOf(::SmtpTopBarMapper)
        singleOf(::SmtpBottomBarMapper)
        singleOf(::SmtpEmailNameMapper)
        singleOf(::SmtpEmailMapper)
        singleOf(::SmtpEmailHostMapper)
    }
}