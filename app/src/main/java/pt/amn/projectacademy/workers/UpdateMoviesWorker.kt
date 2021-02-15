package pt.amn.projectacademy.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase
import pt.amn.projectacademy.domain.usecases.GetTopRatedMovieUseCase
import pt.amn.projectacademy.utils.MoviesNotifications
import timber.log.Timber
import java.lang.Exception

@HiltWorker
class UpdateMoviesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val workerDependency: WorkerDependency) :
    CoroutineWorker(appContext, workerParams) {

    val notifications = MoviesNotifications(appContext)

    init {
        notifications.initialize()
    }

    override suspend fun doWork(): Result = coroutineScope {
        fetchMovies()
    }

    private suspend fun fetchMovies() : Result {
        try {
            GetMovieListUseCase(workerDependency.repository)
                .execute(FIRST_PAGE)
                .also { result ->
                    if (result.isError) {
                        Timber.d("UpdateMoviesWorker. %s", result.description)
                        Result.failure()
                    }
                }

            showNotification()

            return Result.success()
        } catch (ex: Exception) {
            Timber.d("UpdateMoviesWorker. Error updating database")
            return Result.failure()
        }
    }

    private suspend fun showNotification() {

        GetTopRatedMovieUseCase(workerDependency.repository)
            .execute()
            .also {
                result ->
                if (result.isError) {
                    Timber.d("UpdateMoviesWorker. showNotification. %s", result.description)
                } else {
                    val movie: Movie? = result.dataList.lastOrNull()

                    if (movie != null
                        && movie.id != workerDependency.sharedPreferences.getInt(TOP_MOVIE, -1)
                    ) {
                        workerDependency.sharedPreferences.edit()
                            .putInt(TOP_MOVIE, movie.id)
                            .apply()
                        notifications.showNotification(movie)
                    }
                }
            }
    }

    companion object {
        private const val FIRST_PAGE: Int = 1
        private const val TOP_MOVIE = "top_movie"
    }

}