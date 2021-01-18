package pt.amn.projectacademy.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.amn.projectacademy.domain.domain.Actor

@Serializable
data class ActorDataModel (
    val id: Int,
    val name: String?,

    @SerialName("profile_path")
    val profilePath: String?
)

fun ActorDataModel.toDomainModel(): Actor {

    return Actor(
        id = this.id,
        name = this.name ?: "",
        picture = this.profilePath ?: ""
    )
}