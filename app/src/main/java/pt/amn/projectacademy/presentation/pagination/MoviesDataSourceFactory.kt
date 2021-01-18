package pt.amn.projectacademy.presentation.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase
import pt.amn.projectacademy.presentation.pagination.MoviesDataSource

class MoviesDataSourceFactory(
    private val scope: CoroutineScope, private val interactor: GetMovieListUseCase
    ): DataSource.Factory<Int, Movie>() {

    private val sourceLiveData = MutableLiveData<MoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = MoviesDataSource(scope, interactor)
        sourceLiveData.postValue(source)
        return source
    }

}