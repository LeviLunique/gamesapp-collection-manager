package br.pucpr.appdev.gamesapp.screens

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import br.pucpr.appdev.gamesapp.model.*

class EditGameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: IGameRepository = FirestoreGameRepository()
    private val storage = StorageRepository()

    suspend fun getGameById(id: String) = repo.get(id)

    suspend fun updateGame(
        id: String,
        title: String,
        platform: String,
        rating: Int,
        status: GameStatus,
        newCover: Uri?,
        oldCoverUrl: String
    ) {
        try {
            val coverUrl = if (newCover != null) {
                storage.deleteCoverIfAny(oldCoverUrl)
                storage.uploadCover(id, newCover)
            } else {
                oldCoverUrl
            }

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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}