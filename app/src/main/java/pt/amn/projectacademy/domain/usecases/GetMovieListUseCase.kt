package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.data.repositories.Result
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import timber.log.Timber
import java.io.IOException

class GetMovieListUseCase(private val repository: MoviesRepository) {

    private var genres: List<Genre>? = null

    suspend fun execute(page: Int): Result<Movie> {
        return repository.getPopularMovies(page, getGenres())
    }

    private suspend fun getGenres() : List<Genre> {
        if (genres == null) {
            try {
                genres = repository.getGenres().dataList
            } catch (e: IOException) {
                Timber.d("%s An error happened: $e", GetMovieListUseCase::class.java.simpleName)
            }
        }
        return genres ?: emptyList()
    }

}