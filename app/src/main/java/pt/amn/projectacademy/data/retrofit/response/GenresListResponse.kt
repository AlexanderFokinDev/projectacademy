package pt.amn.projectacademy.data.retrofit.response

import kotlinx.serialization.Serializable
import pt.amn.projectacademy.data.models.GenreDataModel

@Serializable
data class GenresListResponse (
    val genres: List<GenreDataModel>
)