package pt.amn.projectacademy.viewmodels

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.amn.projectacademy.MainApplication
import pt.amn.projectacademy.data.loadMovies
import pt.amn.projectacademy.models.Movie
import java.io.IOException

class MoviesListViewModel() : ViewModel() {

    private val _mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())
    val moviesList: LiveData<List<Movie>> get() = _mutableMoviesList

    init {
        loadMoviesList()
    }

    private fun loadMoviesList() {
        viewModelScope.launch {
            try {
                _mutableMoviesList.value = loadMovies(MainApplication.applicationContext())
            } catch (e: IOException) {
                logExceptionSuspend("viewModelExceptionHandler", e)
            }
        }
    }

    private suspend fun logExceptionSuspend(who: String, throwable: Throwable) = withContext(Dispatchers.Main) {
        Log.e(TAG, "$who::Failed", throwable)
    }

}

private const val TAG = "MoviesListViewModel"