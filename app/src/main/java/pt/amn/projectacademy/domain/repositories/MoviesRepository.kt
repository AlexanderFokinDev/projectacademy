package pt.amn.projectacademy.domain.repositories

import pt.amn.projectacademy.data.repositories.Result
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie

interface MoviesRepository {

    suspend fun getPopularMovies(page: Int = 1, genres: List<Genre>) : Result<Movie>

    suspend fun getGenres() : Result<Genre>

    suspend fun getMovieActors(movieId: Int) : Result<Actor>

    suspend fun getMovie(movieId: Int, genres: List<Genre>) : Result<Movie>

    suspend fun getTopRatedMovie(genres: List<Genre>) : Result<Movie>

}