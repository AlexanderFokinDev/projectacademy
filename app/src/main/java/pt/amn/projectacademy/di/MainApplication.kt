package pt.amn.projectacademy.di

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import pt.amn.projectacademy.BuildConfig
import pt.amn.projectacademy.utils.MOVIES_UPDATE_INTERVAL
import pt.amn.projectacademy.workers.UpdateMoviesWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree());
        }

        startUpdateMoviesWorker()
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

        WorkManager.getInstance(applicationContext).enqueue(request)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .setWorkerFactory(workerFactory)
            .build()
    }

}