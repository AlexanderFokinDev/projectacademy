package pt.amn.projectacademy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.net.toUri
import pt.amn.projectacademy.MainActivity
import pt.amn.projectacademy.R
import pt.amn.projectacademy.domain.models.Movie

interface Notifications {
    fun initialize()
    fun showNotification(movie: Movie)
}

class MoviesNotifications(private val context: Context) : Notifications {

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_NEW_MOVIES, IMPORTANCE_HIGH)
                    .setName("Movies messages")
                    .setDescription("All movies messages")
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(movie: Movie) {

        val contentUri = "https://academy.amn.pt/movie/${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle("Recommended to watch")
            .setContentText(movie.title)
            .setSmallIcon(R.drawable.baseline_live_tv_white_18)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

        notificationManagerCompat.notify(MOVIE_TAG, movie.id, builder.build())

    }

    companion object {
        private const val CHANNEL_NEW_MOVIES = "new_movies"
        private const val MOVIE_TAG = "movie"
        private const val REQUEST_CONTENT = 1
    }

}