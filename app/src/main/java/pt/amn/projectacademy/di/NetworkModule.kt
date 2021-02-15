package pt.amn.projectacademy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideTMDBService() : TMDBService {
        return TMDBService.create()
    }
}