package pt.amn.projectacademy.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie

@Serializable
data class MovieDataModel (
    val adult: Boolean? = false,

    @SerialName("backdrop_path")
    val backdropPath: String? = "",

    @SerialName("genre_ids")
    val genreIDS: List<Int>? = null,

    @SerialName("genres")
    val genres: List<GenreDataModel>? = null,

    val id: Int,

    @SerialName("original_language")
    val originalLanguage: String? = "",

    @SerialName("original_title")
    val originalTitle: String? = "",

    val overview: String? = "",

    @SerialName("poster_path")
    val posterPath: String? = "",

    @SerialName("release_date")
    val releaseDate: String? = "",

    val title: String? = "",

    @SerialName("vote_average")
    val voteAverage: Float? = 0F,

    @SerialName("vote_count")
    val voteCount: Int? = 0
)

fun MovieDataModel.toDomainModel(genres: List<Genre>): Movie {

    val genresMap = genres.associateBy {
            genre -> genre.id
    }

    val genreIDS = mutableListOf<Int>()
    if (this.genres != null) {
        for(genre in this.genres) {
            genreIDS.add(genre.id)
        }
    }
    if(this.genreIDS != null) {
        for(genre in this.genreIDS) {
            genreIDS.add(genre)
        }
    }


    return Movie(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview ?: "",
        poster = this.posterPath ?: "",
        backdrop = this.backdropPath ?: "",
        ratings = this.voteAverage ?: 0F,
        adult = this.adult ?: false,
        voteCount = this.voteCount ?: 0,
        releaseDate = this.releaseDate ?: "",
        genres = genreIDS.map {
            genresMap[it] ?: throw IllegalArgumentException("Genre not found")
        }
    )

}