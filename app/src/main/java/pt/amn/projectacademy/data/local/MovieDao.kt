package pt.amn.projectacademy.data.local

import androidx.room.*

/**
 * The Data Access Object for the MovieEntity class.
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("SELECT * FROM movies ORDER BY title")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getById(id: Int): MovieEntity

    @Query("SELECT * from movies ORDER BY voteAverage DESC LIMIT 1")
    suspend fun getTopRated(): MovieEntity

}