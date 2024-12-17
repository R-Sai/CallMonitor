package com.ramzisai.callmonitor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ramzisai.callmonitor.data.model.CallLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(callLog: CallLogEntity): Long

    @Query("SELECT * FROM ${CallLogEntity.TABLE_NAME}")
    fun getCallLogs(): Flow<List<CallLogEntity>>

    @Query("UPDATE ${CallLogEntity.TABLE_NAME} SET ${CallLogEntity.COLUMN_IS_ONGOING} = :isOngoing WHERE id = :id")
    suspend fun updateCallLogOnGoing(id: Long, isOngoing: Boolean)
}