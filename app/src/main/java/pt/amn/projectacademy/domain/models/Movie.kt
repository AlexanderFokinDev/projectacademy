package pt.amn.projectacademy.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.amn.projectacademy.domain.domain.Genre

@Parcelize
data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    var ratings: Float,
    val voteCount: Int,
    val adult: Boolean,
    val releaseDate: String,
    val genres: List<Genre>
) : Parcelable  {

    fun getReview() : String {
        return "$voteCount Reviews"
    }

    fun getMinimumAge() : String {
        // здесь тоже в ДЗ дают только флаг взрослый это или нет, но точного возраста в json нет
        return if (adult) "18+" else "13+"
    }

    fun getRating() : Float {
        return if (ratings <= 0) 0F else ratings / 2
    }

    fun getTag() : String {
        /*** Прочитал статью про kotlin best practices, понял почему на больших проектах
        it - это плохо и научился переименовывать ***/
        return genres.joinToString(separator = ", ", transform = {genreItem -> genreItem.name})
    }
}