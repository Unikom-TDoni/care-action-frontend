package edu.rpl.careaction.feature.activity_tracker.presentation.worker

import android.content.Context
import androidx.work.*
import edu.rpl.careaction.core.config.UniqueWorkerKey
import java.util.concurrent.TimeUnit

class ActivityTrackerWorkManager {
    fun start(context: Context) {
        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<ActivityTrackerCoroutineWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(1, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                UniqueWorkerKey.ACTIVITY_TRACKER.name,
                ExistingPeriodicWorkPolicy.KEEP, notificationWorkRequest
            )
    }
}