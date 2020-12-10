package pt.amn.projectacademy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    var ratings: Float,
    val adult: Boolean,
    val runtime: Int, // minutes
    val genres: List<Genre>,
    val actors: List<Actor>
) : Parcelable {

    fun getReview() : String {
        //return "$reviewCount Reviews"
        // Пока ставлю заглушку, в ДЗ в loadere такого поля нет почему-то
        return "100 Reviews"
    }

    fun getRuntime() : String {
        return "$runtime min"
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