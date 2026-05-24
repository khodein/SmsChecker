package com.sms.checker.forwarder.feature.sms.data

import com.sms.checker.forwarder.feature.sms.data.mapper.SmsDataMapper
import com.sms.checker.forwarder.feature.sms.db.SmsDao
import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

internal class SmsRepositoryImpl(
    private val dao: SmsDao,
    private val smsDataMapper: SmsDataMapper,
) : SmsRepository {

    override suspend fun setSms(model: SmsModel): Long {
        val entity = smsDataMapper.toEntity(model)
        return dao.insert(entity)
    }
}