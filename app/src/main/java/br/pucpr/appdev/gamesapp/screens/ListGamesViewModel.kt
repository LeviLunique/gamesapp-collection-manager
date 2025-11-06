package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListGamesViewModel (app: Application) : AndroidViewModel(app) {
    private val repo: IGameRepository = FirestoreGameRepository()

    private val _games = MutableStateFlow<List<GameItem>>(emptyList())
    val games: StateFlow<List<GameItem>> = _games

    fun loadGames() = viewModelScope.launch {
        _games.value = repo.list()
    }

    fun delete(item: GameItem) = viewModelScope.launch {
        item.id?.let { repo.delete(it) }
        loadGames()
    }
}