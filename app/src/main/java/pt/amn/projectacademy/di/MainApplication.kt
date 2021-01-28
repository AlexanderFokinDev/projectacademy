package pt.amn.projectacademy.di

import android.app.Application
import android.content.Context
import pt.amn.projectacademy.data.local.AppDatabase
import pt.amn.projectacademy.data.repositories.MoviesRepositoryImpl
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import pt.amn.projectacademy.domain.repositories.MoviesRepository

class MainApplication : Application() {

    init {
        instance = this

        repository = createRepository()
    }

    fun createRepository() : MoviesRepository {
        return MoviesRepositoryImpl(TMDBService.create(), AppDatabase.getInstance())
    }

    companion object {
        private var instance: MainApplication? = null

        private lateinit var repository : MoviesRepository

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun getRepository() : MoviesRepository {
            return repository
        }
    }
}