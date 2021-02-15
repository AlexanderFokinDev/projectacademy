package pt.amn.projectacademy.workers

import android.content.SharedPreferences
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import javax.inject.Inject

class WorkerDependency @Inject constructor(var repository: MoviesRepository
                , val sharedPreferences: SharedPreferences) {
}