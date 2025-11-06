package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.gamesapp.model.*
import kotlinx.coroutines.launch

class EditGameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: IGameRepository = FirestoreGameRepository()
    private val storage = StorageRepository()

    suspend fun getGameById(id: String) = repo.get(id)

    fun updateGame(
        id: String,
        title: String,
        platform: String,
        rating: Int,
        status: GameStatus,
        newCover: Uri?,
        oldCoverUrl: String
    ) {
        viewModelScope.launch {
            val coverUrl = if (newCover != null) {
                storage.deleteCoverIfAny(oldCoverUrl)
                storage.uploadCover(id, newCover)
            } else oldCoverUrl

            repo.upsert(
                GameItem(
                    id = id,
                    title = title,
                    platform = platform,
                    rating = rating,
                    status = status,
                    coverUrl = coverUrl
                )
            )
        }
    }
}