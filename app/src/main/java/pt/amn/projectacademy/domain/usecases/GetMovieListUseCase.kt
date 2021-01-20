package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import java.io.IOException

class GetMovieListUseCase(private val repository: MoviesRepository) {

    private var genres: List<Genre>? = null

    sealed class Result {
        data class Success(val data : List<Movie>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    suspend fun execute(page: Int): Result {
        return try {
            Result.Success(repository.getPopularMovies(page, getGenres()))
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    private suspend fun getGenres() : List<Genre> {
        if (genres == null) {
            try {
                genres = repository.getGenres()
            } catch (e: IOException) {
                //throw IllegalArgumentException("Genre not found")
                Result.Error(e)
            }
        }
        return genres ?: emptyList()
    }

}