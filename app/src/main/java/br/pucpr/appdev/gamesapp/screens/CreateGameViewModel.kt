package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.*
import kotlinx.coroutines.launch

class CreateGameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: IGameRepository = FirestoreGameRepository()
    private val storage = StorageRepository()

    fun saveGame(
        title: String,
        platform: String,
        rating: Int,
        status: GameStatus,
        localCoverUri: Uri?
    ) {
        viewModelScope.launch {
            val tempId = repo.upsert(GameItem(title = title, platform = platform, rating = rating, status = status))
            val cover = if (localCoverUri != null) storage.uploadCover(tempId, localCoverUri) else ""
            repo.upsert(GameItem(id = tempId, title = title, platform = platform, rating = rating, status = status, coverUrl = cover))
        }
    }
}