package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.AppDatabase
import br.pucpr.appdev.gamesapp.model.GameEntity
import br.pucpr.appdev.gamesapp.model.GameRepository
import kotlinx.coroutines.launch

class CreateGameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(AppDatabase.getDatabase(app).gameDao())

    fun saveGame(title: String, platform: String, rating: Int) {
        viewModelScope.launch {
            repo.insert(GameEntity(title = title, platform = platform, rating = rating, status = "PLAYING"))
        }
    }
}