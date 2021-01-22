package pt.amn.projectacademy.data.local

import androidx.room.*

/**
 * The Data Access Object for the GenreEntity class.
 */
@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Update
    suspend fun update(genre: GenreEntity)

    @Delete
    suspend fun delete(genre: GenreEntity)

    @Query("SELECT * FROM genres ORDER BY id")
    suspend fun getAll(): List<GenreEntity>

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun getById(id: Int): GenreEntity
}