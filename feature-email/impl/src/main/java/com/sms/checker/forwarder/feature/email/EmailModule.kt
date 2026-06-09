package com.sms.checker.forwarder.feature.email

import com.sms.checker.forwarder.feature.email.data.EmailRepositoryImpl
import com.sms.checker.forwarder.feature.email.data.mapper.SmtpEmailDataMapper
import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.usecase.DeleteSmtpConfigByIdUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.DeleteSmtpConfigByIdUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpConfigUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpIdsUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetEnabledSmtpIdsUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigByIdUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigByIdUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.SaveSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.SaveSmtpConfigUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.SendSmtpMessageUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.SendSmtpMessageUseCaseImpl
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCaseImpl
import com.sms.checker.forwarder.feature.email.infrastructure.SmtpEmailDelegate
import com.sms.checker.forwarder.feature.email.infrastructure.SmtpEmailDelegateImpl
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper.SmtpBottomBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.SmtpEmailNameBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.mapper.SmtpEmailNameMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.SmtpEmailFromBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.mapper.SmtpEmailFromMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.SmtpEmailRecipientBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.mapper.SmtpEmailRecipientMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.SmtpEmailServerBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.mapper.SmtpEmailServerMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.SmtpEmailTestBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.mapper.SmtpEmailTestMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.mapper.SmtpTopBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper.SmtpEmailMapper
import com.sms.checker.forwarder.feature.email.router.EmailProviderImpl
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.email.router.EmailRouterImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object EmailModule {

    fun get() = module {
        // data
        singleOf(::EmailRepositoryImpl) bind EmailRepository::class
        singleOf(::SmtpEmailDataMapper)

        // domain
        singleOf(::SendSmtpMessageUseCaseImpl) bind SendSmtpMessageUseCase::class
        singleOf(::SaveSmtpConfigUseCaseImpl) bind SaveSmtpConfigUseCase::class
        singleOf(::UpdateSmtpConfigUseCaseImpl) bind UpdateSmtpConfigUseCase::class
        singleOf(::GetSmtpConfigByIdUseCaseImpl) bind GetSmtpConfigByIdUseCase::class
        singleOf(::GetSmtpConfigUseCaseImpl) bind GetSmtpConfigUseCase::class
        singleOf(::GetEnabledSmtpConfigUseCaseImpl) bind GetEnabledSmtpConfigUseCase::class
        singleOf(::GetEnabledSmtpIdsUseCaseImpl) bind GetEnabledSmtpIdsUseCase::class
        singleOf(::DeleteSmtpConfigByIdUseCaseImpl) bind DeleteSmtpConfigByIdUseCase::class
        singleOf(::GetEnabledSmtpIdsUseCaseImpl) bind GetEnabledSmtpIdsUseCase::class

        // navigation
        singleOf(::EmailRouterImpl) bind EmailRouter::class
        singleOf(::EmailProviderImpl) bind Router.Provider::class

        // presentation
        viewModel { params ->
            SmtpEmailViewModel(
                smtpId = params.getOrNull<Long>(),
                smtpBottomBarBlock = get(),
                smtpTopBarBlock = get(),
                smtpEmailNameBlock = get(),
                smtpEmailServerBlock = get(),
                smtpEmailFromBlock = get(),
                smtpEmailRecipientBlock = get(),
                smtpEmailTestBlock = get(),
                saveSmtpConfigUseCase = get(),
                updateSmtpConfigUseCase = get(),
                getSmtpConfigByIdUseCase = get(),
                deleteSmtpConfigByIdUseCase = get(),
                router = get(),
                mapper = get(),
            )
        }
        factoryOf(::SmtpBottomBarBlock)
        factoryOf(::SmtpTopBarBlock)
        factoryOf(::SmtpEmailNameBlock)
        factoryOf(::SmtpEmailServerBlock)
        factoryOf(::SmtpEmailFromBlock)
        factoryOf(::SmtpEmailRecipientBlock)
        factoryOf(::SmtpEmailTestBlock)

        // infrastructure
        factoryOf(::SmtpEmailDelegateImpl) bind SmtpEmailDelegate::class

        // mappers
        singleOf(::SmtpTopBarMapper)
        singleOf(::SmtpBottomBarMapper)
        singleOf(::SmtpEmailNameMapper)
        singleOf(::SmtpEmailServerMapper)
        singleOf(::SmtpEmailFromMapper)
        singleOf(::SmtpEmailRecipientMapper)
        singleOf(::SmtpEmailTestMapper)
        singleOf(::SmtpEmailMapper)
    }
}