package pt.amn.projectacademy.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie

@Serializable
data class MovieDataModel (
    val adult: Boolean?,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("genre_ids")
    val genreIDS: List<Int>,

    val id: Int,

    @SerialName("original_language")
    val originalLanguage: String?,

    @SerialName("original_title")
    val originalTitle: String?,

    val overview: String?,
    val popularity: Float,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("release_date")
    val releaseDate: String?,

    val title: String?,
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Float?,

    @SerialName("vote_count")
    val voteCount: Int?
)

fun MovieDataModel.toDomainModel(genres: List<Genre>): Movie {

    val genresMap = genres.associateBy {
            genre -> genre.id
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
        genres = this.genreIDS.map {
            genresMap[it] ?: throw IllegalArgumentException("Genre not found")
        }
    )

}