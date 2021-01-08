package pt.amn.projectacademy.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.CoroutineScope
import pt.amn.projectacademy.data.MoviesDataSourceFactory
import pt.amn.projectacademy.data.MoviesRepository
import pt.amn.projectacademy.models.Actor
import pt.amn.projectacademy.models.Movie

class MoviesInteractor(private val repository: MoviesRepository, scope: CoroutineScope) {

    private val sourceFactory = MoviesDataSourceFactory(repository, scope)

    fun getPopularMovies() : LiveData<PagedList<Movie>> {
        return sourceFactory.toLiveData(
            pageSize = PAGE_SIZE
        )
    }

    suspend fun getMovieActors(movieId: Int) : List<Actor> {
        return repository.getMovieActors(movieId)
    }

    companion object {
        private const val PAGE_SIZE: Int = 20
    }

}