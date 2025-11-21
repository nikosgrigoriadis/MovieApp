package com.example.movieapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.movieapp.database.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val movieTitle = intent.getStringExtra("movie_title") ?: "Movie Reminder"
        val movieId = intent.getIntExtra("movie_id", -1)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Movie Reminder: $movieTitle")
            .setContentText("Time to watch your scheduled movie!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Time to watch \"$movieTitle\"! Enjoy your movie!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(movieTitle.hashCode(), notification)

        autoDeleteScheduled(context, movieId)
    }

    fun autoDeleteScheduled(context: Context, movieId: Int) {
        if (movieId != -1) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val dao = DatabaseProvider.getDatabase(context).movieScheduleDao()
                    dao.deleteByMovieId(movieId)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "movie_reminder_channel"
    }
}
