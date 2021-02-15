package pt.amn.projectacademy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.amn.projectacademy.utils.DATABASE_NAME

@Database(entities = [MovieEntity::class, ActorEntity::class, GenreEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun actorDao(): ActorDao
    abstract fun genreDao(): GenreDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: Room.databaseBuilder(
                context
                , AppDatabase::class.java
                , DATABASE_NAME)
                .build()
        }

    }
}