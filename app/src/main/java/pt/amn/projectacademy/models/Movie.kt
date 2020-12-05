package pt.amn.projectacademy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    val name: String?,
    val minimumAge: Int,
    val duration: Int, // minutes
    var rating: Double = 0.0,
    var reviewCount: Int = 0,
    val tag: String? = "", // list of ganres, for example
    val imagePath: Int,
    val like: Boolean = false
) : Parcelable {

    fun getReview() : String {
        return "${reviewCount} Reviews"
    }

    fun getRatingFloat() : Float {
        return rating.toFloat()
    }

    fun getDuration() : String {
        return "${duration} min"
    }

    fun getMinimumAge() : String {
        return "${minimumAge}+"
    }
}