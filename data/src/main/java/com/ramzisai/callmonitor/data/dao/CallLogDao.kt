package com.ramzisai.callmonitor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ramzisai.callmonitor.data.model.CallLogDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(callLog: CallLogDataModel): Long

    @Query("SELECT * FROM ${CallLogDataModel.TABLE_NAME}")
    fun getCallLogs(): Flow<List<CallLogDataModel>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCallLogs(callLogEntities: List<CallLogDataModel>)

    @Query("UPDATE ${CallLogDataModel.TABLE_NAME} SET ${CallLogDataModel.COLUMN_IS_ONGOING} = :isOngoing WHERE id = :id")
    suspend fun updateCallLogOnGoing(id: Long, isOngoing: Boolean)

    @Query("UPDATE ${CallLogDataModel.TABLE_NAME} SET ${CallLogDataModel.COLUMN_DURATION} = :duration WHERE id = :id")
    suspend fun updateCallLogDuration(id: Long, duration: Long)
}