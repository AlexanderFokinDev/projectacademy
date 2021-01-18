package pt.amn.projectacademy.presentation.pagination

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.*
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase

class MoviesDataSource(
    private val scope: CoroutineScope, private val interactor: GetMovieListUseCase
    ): PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        executeQuery(page = FIRST_PAGE) { movieList ->
            callback.onResult(movieList, null, FIRST_PAGE + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val page = params.key
        executeQuery(page) { movieList ->
            callback.onResult(movieList, page + 1)
        }
    }

    private fun executeQuery(page: Int, callback:(List<Movie>) -> Unit) {

        scope.launch {
            interactor.execute(page)
                .also { result ->
                    when (result) {
                        is GetMovieListUseCase.Result.Success ->
                            callback(result.data)
                        is GetMovieListUseCase.Result.Error -> {
                            Log.e(MoviesDataSource::class.java.simpleName, "An error happened: $result")
                            callback(emptyList())
                        }
                    }
                }
        }
    }

    companion object {
        private const val FIRST_PAGE: Int = 1
    }

}