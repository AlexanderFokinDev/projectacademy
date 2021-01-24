package pt.amn.projectacademy.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.amn.projectacademy.domain.domain.Actor

@Entity(tableName = "actors")
data class ActorEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val name: String? = "",
    val profilePath: String? = ""
)

fun ActorEntity.toDomainModel(): Actor {

    return Actor(
        id = this.id,
        name = this.name ?: "",
        picture = this.profilePath ?: ""
    )
}

fun Actor.toEntityModel(): ActorEntity {
    return ActorEntity(
        id = this.id,
        name = this.name,
        profilePath = this.picture
    )
}