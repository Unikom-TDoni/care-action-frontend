package edu.rpl.careaction.feature.notification.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import edu.rpl.careaction.core.presentation.ApplicationActivity
import edu.rpl.careaction.feature.notification.NotificationGenerator

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
) :
    Worker(context, workerParams) {

    private val notificationGenerator = NotificationGenerator(
        context,
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, ApplicationActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )

    override fun doWork(): Result {
        notificationGenerator.showNotification()
        return Result.success()
    }
}