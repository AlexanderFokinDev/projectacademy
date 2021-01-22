package pt.amn.projectacademy.data.local

import androidx.room.*

/**
 * The Data Access Object for the ActorEntity class.
 */
@Dao
interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: ActorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actors: List<ActorEntity>)

    @Update
    suspend fun update(actor: ActorEntity)

    @Delete
    suspend fun delete(actor: ActorEntity)

    @Query("SELECT * FROM actors ORDER BY name")
    suspend fun getAll(): List<ActorEntity>

    @Query("SELECT * FROM actors WHERE id = :id")
    suspend fun getById(id: Int): ActorEntity
}