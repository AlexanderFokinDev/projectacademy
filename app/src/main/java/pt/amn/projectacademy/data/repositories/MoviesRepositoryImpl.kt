package pt.amn.projectacademy.data.repositories

import android.util.Log
import pt.amn.projectacademy.data.local.AppDatabase
import pt.amn.projectacademy.data.local.toDomainModel
import pt.amn.projectacademy.data.local.toEntityModel
import pt.amn.projectacademy.data.models.toDomainModel
import pt.amn.projectacademy.data.retrofit.service.TMDBService
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import java.io.IOException

class MoviesRepositoryImpl(
    private val service: TMDBService,
    private val database: AppDatabase
) : MoviesRepository {

    override suspend fun getPopularMovies(page: Int, genres: List<Genre>): List<Movie> {

        /** Сначала получаем данные из локальной БД SQLite.
         *  Затем делаем запрос по Api, если оишбок при получении данных не будет, то
         *  на UI попадут данные из API. Если данные получить не удастся (например, Wifi отключен),
         *  то на UI попадут фильмы из локальной БД.
         */
        var movieList: List<Movie> = database.movieDao().getAll()
            .map { movieEntity ->
                movieEntity.toDomainModel(genres)
            }

        try {
            movieList = service.getPopularMoviesAsync(page)
                .results
                .map { movieDataModel ->
                    movieDataModel.toDomainModel(genres)
                }

            database.movieDao().insertAll(
                movieList.map { movie ->
                    movie.toEntityModel()
                }
            )

        } catch (e: IOException) {
            Log.e(MoviesRepositoryImpl::class.java.simpleName, "An error happened: $e")
        }

        return movieList
    }

    override suspend fun getGenres(): List<Genre> {

        var genres: List<Genre> = database.genreDao().getAll()
            .map { genreEntity ->
                genreEntity.toDomainModel()
            }

        try {
            genres = service.getGenresAsync()
                .genres
                .map { genreDataModel ->
                    genreDataModel.toDomainModel()
                }

            database.genreDao().insertAll(
                genres.map { genre ->
                    genre.toEntityModel()
                }
            )
        } catch (e: IOException) {
            Log.e(MoviesRepositoryImpl::class.java.simpleName, "An error happened: $e")
        }

        return genres
    }

    override suspend fun getMovieActors(movieId: Int): List<Actor> {

        var actors: List<Actor> = database.actorDao().getAll()
            .map { actorEntity ->
                actorEntity.toDomainModel()
            }

        try {
            actors = service.getMovieActorsAsync(movieId)
                .cast
                .map { actorDataModel ->
                    actorDataModel.toDomainModel()
                }

            database.actorDao().insertAll(
                actors.map { actor ->
                    actor.toEntityModel()
                }
            )
        } catch (e: IOException) {
            Log.e(MoviesRepositoryImpl::class.java.simpleName, "An error happened: $e")
        }

        return actors
    }

}

