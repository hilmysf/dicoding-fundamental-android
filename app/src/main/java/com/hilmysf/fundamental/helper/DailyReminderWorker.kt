package com.hilmysf.fundamental.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hilmysf.fundamental.R
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.usecase.GetOneEventUseCase
import com.hilmysf.fundamental.presentation.detail.DetailEventActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context, @Assisted params: WorkerParameters,
    private val getOneEventUseCase: GetOneEventUseCase
) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val result = getOneEventUseCase().filter { it is ResultState.Success }
                .first()
            if (result is ResultState.Success) {
                val event = result.data
                showNotification(
                    event.name,
                    DateHelper.formatDate(event.beginTime) ?: event.beginTime,
                    event.id
                )
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String, eventId: Int) {
        try {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "daily_reminder_channel"
            val notificationId = 1

            val intent = Intent(applicationContext, DetailEventActivity::class.java).apply {
                putExtra(DetailEventActivity.EXTRA_EVENT_ID, eventId)
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, flags)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Daily Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notificationManager.notify(notificationId, notificationBuilder.build())
        } catch (e: Exception) {
        }
    }
}