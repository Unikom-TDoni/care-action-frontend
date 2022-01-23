package edu.rpl.careaction.feature.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.rpl.careaction.R

class NotificationGenerator(
    private val context: Context,
    private val pendingIntent: PendingIntent
) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "CareActionNotification"
    }

    init {
        createNotificationChannel()
    }

    private val notifications: Map<Int, Notification> = mapOf(
        1 to generateDefaultBuilder()
            .setContentTitle("Have you eat healthy food today?")
            .setContentText("Check out our healthy food recipes, right now!")
            .build(),
        2 to generateDefaultBuilder()
            .setContentTitle("Are you ready to have a healthy lifestyle?")
            .setContentText("Check out our healthy living program, right now!")
            .build(),
        3 to generateDefaultBuilder()
            .setContentTitle("Have you exercised today?")
            .setContentText("Check out our training program, right now!")
            .build(),
        4 to generateDefaultBuilder()
            .setContentTitle("Do you have an ideal weight?")
            .setContentText("Check your bmi score, right now!")
            .build(),
        5 to generateDefaultBuilder()
            .setContentTitle("Drinking coffee can reduce depression!")
            .setContentText("Find more healthy living tips, right now!")
            .build()
    )

    fun showNotification() =
        notifications.entries.random().also {
            NotificationManagerCompat.from(context).notify(it.key, it.value)
        }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun generateDefaultBuilder() =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
}