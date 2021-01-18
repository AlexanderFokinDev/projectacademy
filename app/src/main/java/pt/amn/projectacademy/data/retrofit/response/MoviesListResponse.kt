package pt.amn.projectacademy.data.retrofit.response

import kotlinx.serialization.Serializable
import pt.amn.projectacademy.data.models.MovieDataModel

@Serializable
data class MoviesListResponse (
    val page: Int,
    val results: List<MovieDataModel>
)