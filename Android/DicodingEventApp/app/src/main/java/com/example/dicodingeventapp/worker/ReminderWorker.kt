package com.example.dicodingeventapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingeventapp.DetailActivity
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.api.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "daily_reminder_channel"
        const val CHANNEL_NAME = "Daily Reminder"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService()
                    .getLatestEvent(active = -1, limit = 1)
            }

            val event = response.listEvents?.firstOrNull()

            if (event != null) {
                showNotification(
                    id = event.id,
                    name = event.name,
                    time = event.beginTime,
                    image = event.imageLogo,
                    owner = event.ownerName,
                    quota = event.quota - event.registrants,
                    description = event.description,
                    link = event.link
                )
            }

            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun showNotification(
        id: Int,
        name: String,
        time: String,
        image: String?,
        owner: String?,
        quota: Int,
        description: String?,
        link: String?
    ) {
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = applicationContext.checkSelfPermission(
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) return
        }

        val intent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID, id)
            putExtra(DetailActivity.EXTRA_NAME, name)
            putExtra(DetailActivity.EXTRA_TIME, time)
            putExtra(DetailActivity.EXTRA_IMAGE, image)
            putExtra(DetailActivity.EXTRA_OWNER, owner)
            putExtra(DetailActivity.EXTRA_QUOTA, quota)
            putExtra(DetailActivity.EXTRA_DESC, description)
            putExtra(DetailActivity.EXTRA_LINK, link)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Event Aktif Terdekat")
            .setContentText("$name - $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi event aktif terdekat"
            }

            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }
}