package com.truckhisaab.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.truckhisaab.R
import com.truckhisaab.data.local.dao.DocumentDao
import com.truckhisaab.data.local.dao.NotificationDao
import com.truckhisaab.data.local.entity.NotificationEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DocumentExpiryWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val documentDao: DocumentDao,
    private val notificationDao: NotificationDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        createNotificationChannel()

        val thresholds = listOf(30, 15, 7, 1)
        val now = System.currentTimeMillis()

        for (days in thresholds) {
            val threshold = now + days.toLong() * 24 * 60 * 60 * 1000
            val nearExpiry = documentDao.getExpiringDocumentsSync(threshold)
            for (doc in nearExpiry) {
                val daysLeft = ((doc.expiryDate - now) / (1000 * 60 * 60 * 24)).toInt()
                if (daysLeft in 0..days) {
                    val title = "${doc.type} - ${doc.truckNumber}"
                    val message = if (daysLeft <= 0) "Document expired!" else "$daysLeft din mein expire hoga"

                    notificationDao.insert(NotificationEntity(
                        type = "DOCUMENT", title = title, message = message,
                        targetRoute = "document/${doc.id}"
                    ))

                    showSystemNotification(title, message, doc.id.toInt())
                }
            }
        }
        return Result.success()
    }

    private fun showSystemNotification(title: String, message: String, id: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        manager.notify(id, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Document Alerts", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Document expiry reminders"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "document_expiry"
        const val WORK_NAME = "document_expiry_check"

        fun enqueueDaily(context: Context) {
            val request = PeriodicWorkRequestBuilder<DocumentExpiryWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }
}
