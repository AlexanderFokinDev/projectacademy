package pt.amn.projectacademy.data.repositories

import pt.amn.projectacademy.data.local.AppDatabase
import pt.amn.projectacademy.data.local.toDomainModel
import pt.amn.projectacademy.data.local.toEntityModel
import pt.amn.projectacademy.data.models.toDomainModel
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import timber.log.Timber
import java.io.IOException

class MoviesRepositoryImpl(
    private val service: TMDBService,
    private val database: AppDatabase
) : MoviesRepository {

    override suspend fun getPopularMovies(page: Int, genres: List<Genre>): Result<Movie> {

        return try {
            val movieList = fetchMoviesFromNetwork(page, genres)
            saveMoviesInDatabase(movieList)
            Result(false, movieList)
        } catch (e: IOException) {
            Timber.d("%s An error happened: $e", MoviesRepositoryImpl::class.java.simpleName)
            Result(true, fetchMoviesFromDatabase(genres), "Network data not available")
        }

    }

    override suspend fun getGenres(): Result<Genre> {

        return try {
            val genres = fetchGenresFromNetwork()
            saveGenresInDatabase(genres)
            Result(false, genres)
        } catch (e: IOException) {
            Timber.d("%s An error happened: $e", MoviesRepositoryImpl::class.java.simpleName)
            Result(true, fetchGenresFromDatabase(), "Network data not available")
        }

    }

    override suspend fun getMovieActors(movieId: Int): Result<Actor> {

        return try {
            val actors = fetchActorsFromNetwork(movieId)
            saveActorsInDatabase(actors)
            Result(false, actors)
        } catch (e: IOException) {
            Timber.d("%s An error happened: $e", MoviesRepositoryImpl::class.java.simpleName)
            Result(true, fetchActorsFromDatabase(), "Network data not available")
        }

    }

    private suspend fun fetchGenresFromDatabase() : List<Genre> {
        return database.genreDao().getAll()
            .map { genreEntity ->
                genreEntity.toDomainModel()
            }
    }

    private suspend fun fetchActorsFromDatabase() : List<Actor> {
        return database.actorDao().getAll()
            .map { actorEntity ->
                actorEntity.toDomainModel()
            }
    }

    private suspend fun fetchMoviesFromDatabase(genres: List<Genre>) : List<Movie> {
        return database.movieDao().getAll()
            .map { movieEntity ->
                movieEntity.toDomainModel(genres)
            }
    }

    private suspend fun fetchGenresFromNetwork() : List<Genre> {
        return service.getGenresAsync()
            .genres
            .map { genreDataModel ->
                genreDataModel.toDomainModel()
            }
    }

    private suspend fun fetchActorsFromNetwork(movieId: Int) : List<Actor> {
        return service.getMovieActorsAsync(movieId)
            .cast
            .map { actorDataModel ->
                actorDataModel.toDomainModel()
            }
    }

    private suspend fun fetchMoviesFromNetwork(page: Int, genres: List<Genre>) : List<Movie> {
        return service.getPopularMoviesAsync(page)
            .results
            .map { movieDataModel ->
                movieDataModel.toDomainModel(genres)
            }
    }

    private suspend fun saveGenresInDatabase(genres: List<Genre>) {
        database.genreDao().insertAll(
            genres.map { genre ->
                genre.toEntityModel()
            }
        )
    }

    private suspend fun saveActorsInDatabase(actors: List<Actor>) {
        database.actorDao().insertAll(
            actors.map { actor ->
                actor.toEntityModel()
            }
        )
    }

    private suspend fun saveMoviesInDatabase(movies: List<Movie>) {
        database.movieDao().insertAll(
            movies.map { movie ->
                movie.toEntityModel()
            }
        )
    }

}

class Result<T>(
    var isError: Boolean,
    var dataList: List<T>,
    var description: String="") {
}

