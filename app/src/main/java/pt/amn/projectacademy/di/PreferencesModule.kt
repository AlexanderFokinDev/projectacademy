package pt.amn.projectacademy.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(PERSISTANCE_STORAGE_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PERSISTANCE_STORAGE_NAME = "StorageMovies"
    }

}