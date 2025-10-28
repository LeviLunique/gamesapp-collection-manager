package br.pucpr.appdev.gamesapp.model

import android.content.Context
import androidx.room.*
import br.pucpr.appdev.gamesapp.base.Constants

@Entity(tableName = Constants.Db.TABLE_GAMES)
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val platform: String,
    val status: GameStatus,
    val rating: Int,
    val notes: String = ""
)

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY id DESC")
    suspend fun getAll(): List<GameEntity>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): GameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity)

    @Delete
    suspend fun delete(game: GameEntity)
}

@Database(entities = [GameEntity::class], version = Constants.Db.VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.Db.NAME
                ).build().also { INSTANCE = it}
            }
        }
    }
}