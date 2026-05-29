package com.praxis.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.praxis.app.data.local.PendingSyncDao
import com.praxis.app.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pendingSyncDao: PendingSyncDao,
    private val apiService: ApiService,
) : CoroutineWorker(appContext, workerParams) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override suspend fun doWork(): Result {
        val pendingItems = pendingSyncDao.getAll()
        if (pendingItems.isEmpty()) return Result.success()

        for (item in pendingItems) {
            try {
                // TODO: Route to correct API endpoint based on item.endpoint
                when {
                    item.endpoint.contains("checkin") -> {
                        @Suppress("UNCHECKED_CAST")
                        val adapter = moshi.adapter(Map::class.java)
                        val body = (adapter.fromJson(item.body) as? Map<String, Any>) ?: continue
                        // apiService.postCheckin(...)
                    }
                    item.endpoint.contains("notebook") -> {
                        // apiService.createNotebookEntry(...)
                    }
                    item.endpoint.contains("tracker") -> {
                        // apiService.logTracker(...)
                    }
                }
                pendingSyncDao.deleteById(item.id)
            } catch (e: Exception) {
                if (item.retryCount >= 3) {
                    pendingSyncDao.deleteById(item.id)
                } else {
                    pendingSyncDao.incrementRetry(item.id)
                }
            }
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "praxis_sync"
    }
}
