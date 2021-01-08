package pt.amn.projectacademy.data

import kotlinx.serialization.*

@Serializable
data class ActorsListResponse (
    val cast: List<Cast>
)

@Serializable
data class Cast (
    val id: Int,
    val name: String,

    @SerialName("profile_path")
    val profilePath: String? = null,
)