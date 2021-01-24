package pt.amn.projectacademy.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.amn.projectacademy.domain.domain.Genre

@Entity(tableName = "genres")
data class GenreEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val name: String
)

fun GenreEntity.toDomainModel(): Genre {

    return Genre(
        id = this.id,
        name = this.name
    )
}

fun Genre.toEntityModel(): GenreEntity {
    return GenreEntity(
        id = this.id,
        name = this.name
    )
}