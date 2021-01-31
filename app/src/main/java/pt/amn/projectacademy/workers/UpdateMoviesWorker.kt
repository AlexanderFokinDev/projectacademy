package pt.amn.projectacademy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import pt.amn.projectacademy.di.MainApplication
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase
import timber.log.Timber
import java.lang.Exception

class UpdateMoviesWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            GetMovieListUseCase(MainApplication.getRepository())
                .execute(FIRST_PAGE)
                .also { result ->
                    if (result.isError) {
                        Timber.tag(TAG)
                        Timber.e(result.description)
                        Result.failure()
                    }
                }
            Result.success()
        } catch (ex: Exception) {
            Timber.tag(TAG)
            Timber.e(ex, "Error updating database")
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "UpdateMoviesWorker"
        private const val FIRST_PAGE: Int = 1
    }

}