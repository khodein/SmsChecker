package com.sms.checker.forwarder.feature.sms.domain

import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import kotlinx.coroutines.flow.Flow

interface SmsRepository {
    suspend fun setSms(model: SmsModel): Long
    suspend fun updateSms(model: SmsModel)
    suspend fun getById(id: Long): SmsModel
    suspend fun getByIdsWithForwards(ids: List<Long>): List<SmsWithForwardsModel>
    suspend fun getPageWithForwards(limit: Int, offset: Int): List<SmsWithForwardsModel>
    suspend fun getLastWithForwards(): List<SmsWithForwardsModel>
    fun observeLastWithForwards(): Flow<List<SmsWithForwardsModel>>
    suspend fun setSmsForward(model: SmsForwardModel): Long
    suspend fun updateSmsForward(model: SmsForwardModel)
}