package pt.amn.projectacademy.data

import kotlinx.serialization.*

@Serializable
data class GenresListResponse (
    val genres: List<GenreResponse>
)

@Serializable
data class GenreResponse (
    val id: Int,
    val name: String
)