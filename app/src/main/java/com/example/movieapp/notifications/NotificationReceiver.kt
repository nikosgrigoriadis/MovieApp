package com.example.movieapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.example.movieapp.database.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        val movieTitle = intent.getStringExtra("movie_title") ?: "Movie Reminder"
        val movieId = intent.getIntExtra("movie_id", -1)
        val posterUrl = intent.getStringExtra("movie_poster")

        // Notification Poster
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var bitmap: Bitmap? = null
                if (!posterUrl.isNullOrEmpty()) {
                    try {
                        bitmap = Glide.with(context)
                            .asBitmap()
                            .load(posterUrl)
                            .submit()
                            .get()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Movie Reminder")
                    .setContentText("It's time to watch \"$movieTitle\"!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                if (bitmap != null) {
                    builder.setLargeIcon(bitmap)
                    builder.setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null as Bitmap?)
                    )
                } else {
                    builder.setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Time to watch \"$movieTitle\"! Enjoy your movie!")
                    )
                }

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(movieTitle.hashCode(), builder.build())

                if (movieId != -1) {
                    val dao = DatabaseProvider.getDatabase(context).movieScheduleDao()
                    dao.deleteByMovieId(movieId)
                }

            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "movie_reminder_channel"
    }
}
