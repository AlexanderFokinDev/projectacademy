package pt.amn.projectacademy.domain.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(val id: Int, val name: String) : Parcelable
