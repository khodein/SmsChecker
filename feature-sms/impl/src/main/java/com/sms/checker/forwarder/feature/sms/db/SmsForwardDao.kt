package com.sms.checker.forwarder.feature.sms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sms.checker.forwarder.feature.sms.db.entity.SmsForwardEntity

@Dao
interface SmsForwardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SmsForwardEntity): Long

    @Update
    suspend fun update(entity: SmsForwardEntity): Int

    @Query("SELECT * FROM sms_forward_table WHERE sms_id = :smsId")
    suspend fun getBySmsId(smsId: Long): List<SmsForwardEntity>

    @Query(
        "SELECT * FROM sms_forward_table " +
            "WHERE sms_id = :smsId AND type = :type AND config_id = :configId " +
            "LIMIT 1"
    )
    suspend fun getOne(smsId: Long, type: String, configId: Long): SmsForwardEntity?
}