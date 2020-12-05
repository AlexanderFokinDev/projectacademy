package pt.amn.projectacademy

import pt.amn.projectacademy.models.Actor
import pt.amn.projectacademy.models.Movie

object MoviesDataSource {

    fun getMovies() : List<Movie> {
        return listOf(
            Movie("Avengers: End Game", 13, 137, 4.0, 125, "Action, Adventure, Drama", R.drawable.movie1),
            Movie("Tenet", 16, 97, 5.0, 98, "Action, Sci-Fi, Thriller", R.drawable.movie2, true),
            Movie("Black Widow", 13, 102, 4.0, 38, "Action, Adventure, Sci-Fi", R.drawable.movie3),
            Movie("Wonder Woman 1984", 13, 120, 5.0, 74, "Action, Adventure, Fantasy", R.drawable.movie4)
        )
    }

    fun getActors() : List<Actor> {
        return listOf(
            Actor("Robert Downey Jr.", R.drawable.actor1),
            Actor("Chris Evans", R.drawable.actor2),
            Actor("Mark Ruffalo", R.drawable.actor3),
            Actor("Chris Hemsworth", R.drawable.actor4)
        )
    }

}