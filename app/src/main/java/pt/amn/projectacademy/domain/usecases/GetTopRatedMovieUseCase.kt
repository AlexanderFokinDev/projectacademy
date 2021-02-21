package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.data.repositories.Result
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import timber.log.Timber
import java.io.IOException

class GetTopRatedMovieUseCase(private val repository: MoviesRepository) {

    private var genres: List<Genre>? = null

    suspend fun execute(): Result<Movie> {
        return repository.getTopRatedMovie(getGenres())
    }

    private suspend fun getGenres() : List<Genre> {
        if (genres == null) {
            try {
                genres = repository.getGenres().dataList
            } catch (e: IOException) {
                Timber.d("getGenres. An error happened: $e")
            }
        }
        return genres ?: emptyList()
    }

}