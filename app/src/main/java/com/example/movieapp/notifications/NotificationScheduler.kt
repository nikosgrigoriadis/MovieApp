package com.example.movieapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

object NotificationScheduler {

    private const val TAG = "NotificationScheduler"

    fun scheduleNotification(
        context: Context,
        timeInMillis: Long,
        movieTitle: String,
        movieId: Int,
        posterUrl: String?
    ) {
        Log.d(TAG, "Scheduling notification for movie: $movieTitle (ID: $movieId) at $timeInMillis")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("movie_title", movieTitle)
            putExtra("movie_id", movieId)
            putExtra("movie_poster", posterUrl)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            movieTitle.hashCode(), // Unique ID for each movie reminder
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Works even in sleep mode/ no exact alarms
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }
}
