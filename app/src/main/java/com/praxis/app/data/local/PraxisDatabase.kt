package com.praxis.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PendingSync::class],
    version = 1,
    exportSchema = false,
)
abstract class PraxisDatabase : RoomDatabase() {
    abstract fun pendingSyncDao(): PendingSyncDao
}
