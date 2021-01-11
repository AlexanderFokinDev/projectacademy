package pt.amn.projectacademy.data

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.*
import pt.amn.projectacademy.models.Genre
import pt.amn.projectacademy.models.Movie

class MoviesDataSource(private val repository: MoviesRepository, private val scope: CoroutineScope)
    : PageKeyedDataSource<Int, Movie>() {

    private var supervisorJob = SupervisorJob()
    private var genres: List<Genre>? = null

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

        scope.launch(getJobErrorHandler() + supervisorJob) {
            if (genres == null) {
                genres = parseGenresListResponse(repository.getGenres())
            }
            callback(
                parseMoviesListResponse(repository.getPopularMovies(page), genres ?: emptyList())
            )
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(MoviesDataSource::class.java.simpleName, "An error happened: $e")
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()
    }

    companion object {
        private val FIRST_PAGE: Int = 1
    }

}

internal fun parseMoviesListResponse(
    moviesListResponse: MoviesListResponse, genres: List<Genre>
): List<Movie> {

    val genresMap = genres.associateBy { genre -> genre.id }

    return moviesListResponse.results.map { jsonMovie ->
        Movie(
            id = jsonMovie.id,
            title = jsonMovie.title,
            overview = jsonMovie.overview,
            poster = jsonMovie.posterPath,
            backdrop = jsonMovie.backdropPath,
            ratings = jsonMovie.voteAverage,
            adult = jsonMovie.adult,
            voteCount = jsonMovie.voteCount,
            releaseDate = jsonMovie.releaseDate,
            genres = jsonMovie.genreIDS.map {
                genresMap[it] ?: throw IllegalArgumentException("Genre not found")
            },
            actors = emptyList()
        )
    }
}

internal fun parseGenresListResponse(
    genresListResponse: GenresListResponse
): List<Genre> {

    return genresListResponse.genres.map { jsonGenre ->
        Genre(
            id = jsonGenre.id,
            name = jsonGenre.name
            )
    }
}