package edu.rpl.careaction.feature.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import edu.rpl.careaction.core.config.UniqueWorkerKey
import edu.rpl.careaction.feature.notification.worker.NotificationWorker
import java.util.concurrent.TimeUnit

class NotificationWorkManager {
    fun start(context: Context) {
        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(8, TimeUnit.HOURS)
                .setInitialDelay(8, TimeUnit.HOURS)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                UniqueWorkerKey.NOTIFICATION.name,
                ExistingPeriodicWorkPolicy.KEEP, notificationWorkRequest
            )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(UniqueWorkerKey.NOTIFICATION.name)
    }
}