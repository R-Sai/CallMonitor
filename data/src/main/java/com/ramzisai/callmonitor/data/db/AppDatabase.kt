package com.ramzisai.callmonitor.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramzisai.callmonitor.data.dao.CallLogDao
import com.ramzisai.callmonitor.data.model.CallLogDataModel

@Database(entities = [CallLogDataModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun callLogDao(): CallLogDao

    companion object {
        const val DB_NAME = "call_monitor.db"
    }
}