package pt.amn.projectacademy.presentation.viewmodels

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import pt.amn.projectacademy.presentation.pagination.MoviesDataSourceFactory
import pt.amn.projectacademy.di.MainApplication
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase

class MoviesListViewModel() : ViewModel() {

    private val interactor = GetMovieListUseCase(MainApplication.getRepository())

    private var _mutableMoviesList: LiveData<PagedList<Movie>>

    val moviesList: LiveData<PagedList<Movie>> get() {
        return _mutableMoviesList
    }

    init {
        val factory = MoviesDataSourceFactory(viewModelScope, interactor)
        _mutableMoviesList = LivePagedListBuilder<Int, Movie>(factory, PAGE_SIZE)
            .build()
    }

    companion object {
        private const val TAG = "MoviesListViewModel"
        private const val PAGE_SIZE: Int = 20
    }

}