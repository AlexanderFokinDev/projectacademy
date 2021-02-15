package pt.amn.projectacademy.presentation.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.*
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.usecases.GetMovieListUseCase
import pt.amn.projectacademy.presentation.viewmodels.utils.Resource

class MoviesDataSource(
    private val scope: CoroutineScope, private val interactor: GetMovieListUseCase
    ): PageKeyedDataSource<Int, Movie>() {

    private val networkState = MutableLiveData<Resource<String>>()

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
                    if(result.isError) {
                        networkState.postValue(Resource.error(result.description,"error"))
                    } else {
                        networkState.postValue(Resource.success("success"))
                    }
                    callback(result.dataList)
                }
        }
    }

    fun getNetworkState() : LiveData<Resource<String>> {
        return networkState
    }

    companion object {
        private const val FIRST_PAGE: Int = 1
    }

}