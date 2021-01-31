package pt.amn.projectacademy.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.amn.projectacademy.domain.domain.Genre
import pt.amn.projectacademy.domain.models.Movie

@Entity(tableName = "movies")
data class MovieEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Float,
    val adult: Boolean,
    val voteCount: Int,
    val releaseDate: String,
    val genreIDS: String
)

fun MovieEntity.toDomainModel(genres: List<Genre>): Movie {

    val genresMap = genres.associateBy {
            genre -> genre.id
    }

    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster = this.posterPath,
        backdrop = this.backdropPath,
        ratings = this.voteAverage,
        adult = this.adult,
        voteCount = this.voteCount,
        releaseDate = this.releaseDate,

        genres = if (genresMap.isEmpty()) {
            emptyList()
        } else {
            this.genreIDS.split(",")
                .map {
                    genresMap[it.toInt()] ?: throw IllegalArgumentException("Genre not found")
                }
        }
    )

}

fun Movie.toEntityModel(): MovieEntity {

    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.poster,
        backdropPath = this.backdrop,
        voteAverage = this.ratings,
        adult = this.adult,
        voteCount = this.voteCount,
        releaseDate = this.releaseDate,
        genreIDS = this.genres.joinToString(
            separator = ",", transform = { genreItem ->
                genreItem.id.toString()
            }
        )
    )

}