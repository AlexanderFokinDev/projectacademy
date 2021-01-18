package pt.amn.projectacademy.domain.repositories

import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie

interface MoviesRepository {

    suspend fun getPopularMovies(page: Int = 1, genres: List<Genre>) : List<Movie>

    suspend fun getGenres() : List<Genre>

    suspend fun getMovieActors(movieId: Int) : List<Actor>

}