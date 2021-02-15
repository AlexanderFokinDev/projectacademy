package pt.amn.projectacademy.presentation.viewmodels

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import pt.amn.projectacademy.presentation.pagination.MoviesDataSourceFactory
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase
import pt.amn.projectacademy.presentation.viewmodels.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository) : ViewModel() {

    private val interactor = GetMovieListUseCase(repository)
    val factory = MoviesDataSourceFactory(viewModelScope, interactor)
    private var _mutableMoviesList: LiveData<PagedList<Movie>>

    val moviesList: LiveData<PagedList<Movie>> get() {
        return _mutableMoviesList
    }

    val networkState: LiveData<Resource<String>> = switchMap(factory.sourceLiveData) {
        moviesDataSource -> moviesDataSource.getNetworkState()
    }

    init {
        _mutableMoviesList = LivePagedListBuilder<Int, Movie>(factory, PAGE_SIZE)
            .build()
    }

    companion object {
        private const val TAG = "MoviesListViewModel"
        private const val PAGE_SIZE: Int = 20
    }

}