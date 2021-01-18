package pt.amn.projectacademy.data.repositories

import pt.amn.projectacademy.data.models.toDomainModel
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository

class MoviesRepositoryImpl(
    private val service: TMDBService
    ) : MoviesRepository {

    override suspend fun getPopularMovies(page: Int, genres: List<Genre>): List<Movie> {
        return service.getPopularMoviesAsync(page)
            .results
            .map { movieDataModel ->
                movieDataModel.toDomainModel(genres)
            }
    }

    override suspend fun getGenres(): List<Genre> {
        return service.getGenresAsync()
            .genres
            .map { genreDataModel ->
                genreDataModel.toDomainModel()
            }
    }

    override suspend fun getMovieActors(movieId: Int): List<Actor> {
        return service.getMovieActorsAsync(movieId)
            .cast
            .map { actorDataModel ->
                actorDataModel.toDomainModel()
            }
    }

}

