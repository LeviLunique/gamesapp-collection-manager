package br.pucpr.appdev.gamesapp.model

import android.content.Context
import androidx.room.*

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val platform: String,
    val status: String,
    val rating: Int,
    val notes: String = ""
)

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY id DESC")
    suspend fun getAll(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity)

    @Delete
    suspend fun delete(game: GameEntity)
}

@Database(entities = [GameEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "games_db"
                ).build().also { INSTANCE = it}
            }
        }
    }
}