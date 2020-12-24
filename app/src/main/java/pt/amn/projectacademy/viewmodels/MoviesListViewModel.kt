package pt.amn.projectacademy.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.amn.projectacademy.data.loadMovies
import pt.amn.projectacademy.models.Movie
import java.io.IOException

/** Для загрузки фильмов из JSON необходимо передать context в функцию loadMovies.
 Т. к. передача Activity в модель в качестве контекста может привести к утечкам памяти,
 то выбрал способ через наследование не от ViewModel, а от AndroidViewModel
 (нашел такой вариант решения в курсе от StartAndroid) */
class MoviesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())
    val moviesList: LiveData<List<Movie>> get() = _mutableMoviesList

    init {
        loadMoviesList()
    }

    private fun loadMoviesList() {
        viewModelScope.launch {
            try {
                _mutableMoviesList.value = loadMovies(getApplication())
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