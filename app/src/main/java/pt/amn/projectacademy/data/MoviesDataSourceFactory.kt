package pt.amn.projectacademy.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import pt.amn.projectacademy.models.Movie

class MoviesDataSourceFactory(private val repository: MoviesRepository
        , private val scope: CoroutineScope): DataSource.Factory<Int, Movie>() {

    val sourceLiveData = MutableLiveData<MoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = MoviesDataSource(repository, scope)
        sourceLiveData.postValue(source)
        return source
    }

}