package pt.amn.projectacademy.data.repositories

import android.content.SharedPreferences
import pt.amn.projectacademy.data.local.AppDatabase
import pt.amn.projectacademy.data.local.AppPreferences
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val service: TMDBService,
    private val database: AppDatabase,
    private val preferences: AppPreferences
) : MoviesRepository {

    override suspend fun getPopularMovies(page: Int, genres: List<Genre>): Result<Movie> =
        try {
            val movieList = fetchMoviesFromNetwork(page, genres)
            saveMoviesInDatabase(movieList)
            Result(false, movieList)
        } catch (e: IOException) {
            Timber.d("getPopularMovies. An error happened: $e")
            Result(true, fetchMoviesFromDatabase(genres), "Network data not available")
        }

    override suspend fun getGenres(): Result<Genre> =
        try {
            val genres = fetchGenresFromNetwork()
            saveGenresInDatabase(genres)
            Result(false, genres)
        } catch (e: IOException) {
            Timber.d("getGenres. An error happened: $e")
            Result(true, fetchGenresFromDatabase(), "Network data not available")
        }

    override suspend fun getMovieActors(movieId: Int): Result<Actor> =
        try {
            val actors = fetchActorsFromNetwork(movieId)
            saveActorsInDatabase(actors)
            Result(false, actors)
        } catch (e: IOException) {
            Timber.d("getMovieActors. An error happened: $e")
            Result(true, fetchActorsFromDatabase(), "Network data not available")
        }


    override suspend fun getMovie(movieId: Int, genres: List<Genre>): Result<Movie> =
        try {
            val movie = fetchMovieFromNetwork(movieId, genres)
            saveMovieInDatabase(movie)
            Result(false, listOf(movie))
        } catch (e: IOException) {
            Timber.d("getMovie. An error happened: $e")
            Result(
                true, fetchMovieFromDatabase(movieId, genres),
                "Network data not available"
            )
        }

    override suspend fun getTopRatedMovie(genres: List<Genre>): Result<Movie> =
        try {
            Result(false, listOf(getTopRatedFromDatabase(genres)))
        } catch (e: IOException) {
            Timber.d("getTopRatedMovie. An error happened: $e")
            Result(
                true, emptyList(),
                "getTopRatedMovie. An error happened"
            )
        }

    private suspend fun fetchGenresFromDatabase(): List<Genre> =
        database.genreDao().getAll()
            .map { genreEntity ->
                genreEntity.toDomainModel()
            }


    private suspend fun fetchActorsFromDatabase(): List<Actor> =
        database.actorDao().getAll()
            .map { actorEntity ->
                actorEntity.toDomainModel()
            }


    private suspend fun fetchMoviesFromDatabase(genres: List<Genre>): List<Movie> =
        database.movieDao().getAll()
            .map { movieEntity ->
                movieEntity.toDomainModel(genres)
            }


    private suspend fun fetchMovieFromDatabase(movieId: Int, genres: List<Genre>): List<Movie> =
        listOf(database.movieDao().getById(movieId).toDomainModel(genres))

    private suspend fun getTopRatedFromDatabase(genres: List<Genre>): Movie =
        database.movieDao().getTopRated().toDomainModel(genres)


    private suspend fun fetchGenresFromNetwork(): List<Genre> =
        service.getGenresAsync()
            .genres
            .map { genreDataModel ->
                genreDataModel.toDomainModel()
            }

    private suspend fun fetchActorsFromNetwork(movieId: Int): List<Actor> =
        service.getMovieActorsAsync(movieId)
            .cast
            .map { actorDataModel ->
                actorDataModel.toDomainModel()
            }

    private suspend fun fetchMoviesFromNetwork(page: Int, genres: List<Genre>): List<Movie> =
        service.getPopularMoviesAsync(page)
            .results
            .map { movieDataModel ->
                movieDataModel.toDomainModel(genres)
            }


    private suspend fun fetchMovieFromNetwork(movieId: Int, genres: List<Genre>): Movie =
        service.getMovieByIdAsync(movieId)
            .toDomainModel(genres)


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

    private suspend fun saveMovieInDatabase(movie: Movie) {
        database.movieDao().insert(movie.toEntityModel())
    }

    override fun saveCalendarRational(isRationaleCalendarShown: Boolean) {
        preferences.saveCalendarRational(isRationaleCalendarShown)
    }

    override fun getCalendarRational(): Boolean {
        return preferences.getCalendarRational()
    }

}

class Result<T>(
    var isError: Boolean,
    var dataList: List<T>,
    var description: String="") {
}

