package com.ramzisai.callmonitor.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramzisai.callmonitor.data.model.CallLogDataModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CallLogDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = COLUMN_TIMESTAMP) val timestamp: Long,
    @ColumnInfo(name = COLUMN_NUMBER) val number: String?,
    @ColumnInfo(name = COLUMN_NAME) val name: String?,
    @ColumnInfo(name = COLUMN_DURATION) val duration: Long,
    @ColumnInfo(name = COLUMN_TIMES_QUERIED) var timesQueried: Int = 0,
    @ColumnInfo(name = COLUMN_IS_ONGOING) var isOngoing: Boolean
) {
    companion object {
        const val TABLE_NAME = "CallLogEntity"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_NUMBER = "number"
        const val COLUMN_NAME = "name"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_TIMES_QUERIED = "timesQueried"
        const val COLUMN_IS_ONGOING = "isOngoing"
    }
}