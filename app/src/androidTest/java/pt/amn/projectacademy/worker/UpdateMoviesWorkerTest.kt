package pt.amn.projectacademy.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pt.amn.projectacademy.workers.UpdateMoviesWorker


@RunWith(JUnit4::class)
class UpdateMoviesWorkerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testWork() {

        //val worker = TestListenableWorkerBuilder<UpdateMoviesWorker>(context).build()

        //val result = worker.startWork().get()

        //assertThat(result, `is`(Result.success()))

        /*val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED) // Need WiFi
            .build()

        val request = PeriodicWorkRequestBuilder<UpdateMoviesWorker>(
            repeatInterval = MOVIES_UPDATE_INTERVAL, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(request)*/

    }
}