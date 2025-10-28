package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.*
import kotlinx.coroutines.launch

class EditGameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(AppDatabase.getDatabase(app).gameDao())

    suspend fun getGameById(id: Long) = repo.get(id)

    fun updateGame(
        id: Long?,
        title: String,
        platform: String,
        rating: Int,
        status: GameStatus
    ) {
        if (id == null) return
        viewModelScope.launch {
            repo.insert(
                GameEntity(
                    id = id,
                    title = title,
                    platform = platform,
                    rating = rating,
                    status = status
                )
            )
        }
    }
}