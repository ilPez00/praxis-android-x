package com.praxis.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_sync")
data class PendingSync(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val endpoint: String,
    val method: String = "POST",
    val body: String,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
)
