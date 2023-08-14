package com.mypet

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorkerManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val notificationText = inputData.getString(NOTIFICATION_TITLE)
        val notificationNote = inputData.getString(NOTIFICATION_NOTES)

        val notificationId = System.currentTimeMillis().hashCode()
        val notificationBuilder =
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel()
                NotificationCompat.Builder(applicationContext, REMINDER_CHANNEL)
            } else {
                NotificationCompat.Builder(applicationContext)
            }

                .setSmallIcon(R.drawable.dog)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(applicationContext.getString(R.string.reminder))
                .setContentText(notificationText)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(notificationNote)
                )
                .build()



        NotificationManagerCompat.from(applicationContext)
            .notify(notificationId, notificationBuilder)

        return Result.success()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                REMINDER_CHANNEL,
                REMINDER_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }

    companion object {
        const val REMINDER_CHANNEL = "reminders"
        const val NOTIFICATION_NOTES = "notes"
        const val NOTIFICATION_TITLE = "title"
    }
}