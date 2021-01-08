package pt.amn.projectacademy.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagedList
import kotlinx.coroutines.cancel
import pt.amn.projectacademy.api.TMDBService
import pt.amn.projectacademy.data.MoviesRepository
import pt.amn.projectacademy.models.Movie
import pt.amn.projectacademy.usecases.MoviesInteractor

class MoviesListViewModel() : ViewModel() {

    private val interactor = MoviesInteractor(MoviesRepository(TMDBService.create()), viewModelScope)

    private var _mutableMoviesList: LiveData<PagedList<Movie>>? = null

    val moviesList: LiveData<PagedList<Movie>> get() {
        if (_mutableMoviesList == null) {
            _mutableMoviesList = interactor.getPopularMovies()
        }
        return _mutableMoviesList as LiveData<PagedList<Movie>>
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel()
    }

}

private const val TAG = "MoviesListViewModel"