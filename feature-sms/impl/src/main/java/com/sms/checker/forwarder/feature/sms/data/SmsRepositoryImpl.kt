package com.sms.checker.forwarder.feature.sms.data

import androidx.room.RoomDatabase
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsDataMapper
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsForwardDataMapper
import com.sms.checker.forwarder.feature.sms.db.SmsDao
import com.sms.checker.forwarder.feature.sms.db.SmsForwardDao
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity
import com.sms.checker.forwarder.feature.sms.db.entity.SmsForwardEntity
import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.feature.sms.domain.model.exception.SmsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SmsRepositoryImpl(
    private val dao: SmsDao,
    private val smsForwardDao: SmsForwardDao,
    private val database: RoomDatabase,
    private val smsDataMapper: SmsDataMapper,
    private val smsForwardDataMapper: SmsForwardDataMapper,
) : SmsRepository {

    override suspend fun setSms(model: SmsModel): Long {
        val entity = smsDataMapper.toEntity(model)
        return dao.insert(entity)
    }

    override suspend fun updateSms(model: SmsModel) {
        dao.update(smsDataMapper.toEntity(model))
    }

    override suspend fun getById(id: Long): SmsModel {
        val entity = dao.getById(id) ?: throw SmsException.SmsExceptionNotFound(id)
        return smsDataMapper.toModel(entity)
    }

    override suspend fun getByIdsWithForwards(ids: List<Long>): List<SmsWithForwardsModel> {
        return dao.getByIdsWithForwards(ids).map(smsDataMapper::toModel)
    }

    override suspend fun getPageWithForwards(limit: Int, offset: Int): List<SmsWithForwardsModel> {
        return dao.getPageWithForwards(limit, offset).map(smsDataMapper::toModel)
    }

    override suspend fun getLastWithForwards(count: Int): List<SmsWithForwardsModel> {
        return dao.getLastWithForwards(count).map(smsDataMapper::toModel)
    }

    override fun observeLastWithForwards(count: Int): Flow<List<SmsWithForwardsModel>> {
        return database.invalidationTracker
            .createFlow(SmsEntity.TABLE_NAME, SmsForwardEntity.TABLE_NAME, emitInitialState = true)
            .map { dao.getLastWithForwards(count).map(smsDataMapper::toModel) }
    }

    override suspend fun setSmsForward(model: SmsForwardModel): Long {
        val entity = smsForwardDataMapper.toEntity(model)
        return smsForwardDao.insert(entity)
    }

    override suspend fun updateSmsForward(model: SmsForwardModel) {
        smsForwardDao.update(smsForwardDataMapper.toEntity(model))
    }
}