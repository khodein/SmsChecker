package com.sms.checker.forwarder.feature.warning.data

import com.sms.checker.forwarder.feature.warning.R
import com.sms.checker.forwarder.feature.warning.domain.WarningRepository
import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class WarningRepositoryImpl(
    private val resProvider: ResProvider,
) : WarningRepository {

    override fun getWarning(): WarningModel = WarningModel(
        title = resProvider.getString(R.string.feature_warning_title),
        notificationText = resProvider.getString(R.string.feature_warning_notification_text),
        description = resProvider.getString(R.string.feature_warning_dialog_description),
    )
}
