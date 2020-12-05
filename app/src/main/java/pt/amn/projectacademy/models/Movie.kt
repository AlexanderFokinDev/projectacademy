package pt.amn.projectacademy.models

import android.os.Parcel
import android.os.Parcelable

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

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(name)
            writeInt(minimumAge)
            writeInt(duration)
            writeDouble(rating)
            writeInt(reviewCount)
            writeString(tag)
            writeInt(imagePath)
            writeByte(if (like) 1 else 0)
        }
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

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