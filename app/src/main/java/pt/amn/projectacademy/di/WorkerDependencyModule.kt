package pt.amn.projectacademy.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import pt.amn.projectacademy.workers.WorkerDependency
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WorkerDependencyModule {

    @Singleton
    @Provides
    fun provideWorkerDependency(repository: MoviesRepository, sharedPreferences: SharedPreferences)
    : WorkerDependency {
        return WorkerDependency(repository, sharedPreferences)
    }
}