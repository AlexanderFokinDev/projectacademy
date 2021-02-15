package pt.amn.projectacademy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.amn.projectacademy.data.repositories.MoviesRepositoryImpl
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(moviesRepositoryImpl: MoviesRepositoryImpl) : MoviesRepository
}