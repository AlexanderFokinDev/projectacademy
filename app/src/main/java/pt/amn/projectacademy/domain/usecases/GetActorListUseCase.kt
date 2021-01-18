package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import java.io.IOException

class GetActorListUseCase(private val repository: MoviesRepository) {

    sealed class Result {
        data class Success(val data : List<Actor>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    suspend fun execute(
        movieId: Int
    ): Result {
        return try {
            Result.Success(repository.getMovieActors(movieId))
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

}