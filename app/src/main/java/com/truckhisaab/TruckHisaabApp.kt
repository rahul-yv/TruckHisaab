package com.truckhisaab

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.truckhisaab.sync.DailyReminderWorker
import com.truckhisaab.sync.DocumentExpiryWorker
import com.truckhisaab.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TruckHisaabApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        SyncWorker.enqueuePeriodicSync(this)
        DocumentExpiryWorker.enqueueDaily(this)
        DailyReminderWorker.scheduleDaily(this)
    }
}
