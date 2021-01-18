package pt.amn.projectacademy.data.retrofit.response

import kotlinx.serialization.Serializable
import pt.amn.projectacademy.data.models.ActorDataModel

@Serializable
data class ActorsListResponse (
    val cast: List<ActorDataModel>
)