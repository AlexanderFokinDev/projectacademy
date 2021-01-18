package pt.amn.projectacademy.data.models

import kotlinx.serialization.Serializable
import pt.amn.projectacademy.domain.domain.Genre

@Serializable
data class GenreDataModel (
    val id: Int,
    val name: String
)

fun GenreDataModel.toDomainModel(): Genre {

    return Genre(
        id = this.id,
        name = this.name
    )
}