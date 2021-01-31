package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.data.repositories.Result
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.repositories.MoviesRepository

class GetActorListUseCase(private val repository: MoviesRepository) {

    suspend fun execute(
        movieId: Int
    ): Result<Actor> {
        return repository.getMovieActors(movieId)
    }

}