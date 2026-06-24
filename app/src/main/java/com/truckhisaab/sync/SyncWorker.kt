package com.truckhisaab.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.truckhisaab.data.local.dao.SyncQueueDao
import com.truckhisaab.data.remote.TruckHisaabApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncQueueDao: SyncQueueDao,
    private val api: TruckHisaabApi
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val items = syncQueueDao.getItemsToSync()
        if (items.isEmpty()) return Result.success()

        var allSuccess = true
        for (item in items) {
            try {
                val response = api.uploadSyncData(
                    mapOf(
                        "action" to item.action,
                        "entityType" to item.entityType,
                        "entityId" to item.entityId,
                        "payload" to item.payload
                    )
                )
                if (response.isSuccessful) {
                    syncQueueDao.deleteById(item.id)
                } else {
                    syncQueueDao.incrementRetry(item.id)
                    allSuccess = false
                }
            } catch (_: Exception) {
                syncQueueDao.incrementRetry(item.id)
                allSuccess = false
            }
        }

        return if (allSuccess) Result.success() else Result.retry()
    }

    companion object {
        const val WORK_NAME = "truckhisaab_sync"

        fun enqueuePeriodicSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<SyncWorker>(4, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }

        fun enqueueSingleSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
