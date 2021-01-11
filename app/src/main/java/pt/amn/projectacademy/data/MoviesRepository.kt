package pt.amn.projectacademy.data

import pt.amn.projectacademy.api.TMDBService
import pt.amn.projectacademy.models.Actor

class MoviesRepository(private val service: TMDBService) {

    suspend fun getPopularMovies(page: Int = 1) = service.getPopularMovies(page).await()

    suspend fun getGenres() = service.getGenres().await()

    suspend fun getMovieActors(movieId: Int) : List<Actor> {
        return parseActorsListResponse(service.getMovieActors(movieId).await())
    }

}

internal fun parseActorsListResponse(
    actorsListResponse: ActorsListResponse
): List<Actor> {

    return actorsListResponse.cast.map { jsonActor ->
        Actor(
            id = jsonActor.id,
            name = jsonActor.name,
            picture = jsonActor.profilePath ?: ""
        )
    }
}

