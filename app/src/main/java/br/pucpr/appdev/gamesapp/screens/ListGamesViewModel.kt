package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class ListGamesViewModel (app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(AppDatabase.getDatabase(app).gameDao())

    private val _games = MutableStateFlow<List<GameEntity>>(emptyList())
    val games: StateFlow<List<GameEntity>> = _games

    fun loadGames() = viewModelScope.launch {
        _games.value = repo.getAll()
    }

    fun delete(game: GameEntity) = viewModelScope.launch {
        repo.delete(game)
        loadGames()
    }
}