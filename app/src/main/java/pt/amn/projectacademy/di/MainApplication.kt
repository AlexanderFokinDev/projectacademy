package pt.amn.projectacademy.di

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import pt.amn.projectacademy.data.local.AppDatabase
import pt.amn.projectacademy.data.repositories.MoviesRepositoryImpl
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import pt.amn.projectacademy.utils.MOVIES_UPDATE_INTERVAL
import pt.amn.projectacademy.workers.UpdateMoviesWorker
import java.util.concurrent.TimeUnit

class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null
        private var repository : MoviesRepository? = null
        private lateinit var mContext : Context

        fun applicationContext() : Context {
            return mContext
        }

        fun setContext(context: Context) {
            mContext = context
            startUpdateMoviesWorker()
        }

        private fun createRepository() : MoviesRepository {
            return MoviesRepositoryImpl(
                TMDBService.create(),
                AppDatabase.getInstance())
        }

        fun getRepository() : MoviesRepository {
            if (repository == null) {
                repository = createRepository()
            }
            return repository as MoviesRepository
        }

        private fun startUpdateMoviesWorker() {

            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED) // Need WiFi
                .build()

            val request = PeriodicWorkRequestBuilder<UpdateMoviesWorker>(
                repeatInterval = MOVIES_UPDATE_INTERVAL, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(applicationContext()).enqueue(request)
        }

    }
}