package br.pucpr.appdev.gamesapp.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGameRepository : IGameRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun col() = db.collection("users")
        .document(requireNotNull(auth.currentUser?.uid) { "Usuário não autenticado" })
        .collection("games")

    override suspend fun list(): List<GameItem> =
        col().orderBy("title").get().await().documents.map { d ->
            val r = d.toObject(GameRemote::class.java) ?: GameRemote()
            GameItem(
                id = d.id,
                title = r.title,
                platform = r.platform,
                status = GameStatus.valueOf(r.status),
                rating = r.rating,
                notes = r.notes,
                coverUrl = r.coverUrl
            )
        }

    override suspend fun get(id: String): GameItem? {
        val d = col().document(id).get().await()
        if (!d.exists()) return null
        val r = d.toObject(GameRemote::class.java) ?: return null
        return GameItem(
            id = d.id,
            title = r.title,
            platform = r.platform,
            status = GameStatus.valueOf(r.status),
            rating = r.rating,
            notes = r.notes,
            coverUrl = r.coverUrl
        )
    }

    override suspend fun upsert(item: GameItem): String {
        val remote = GameRemote(
            title = item.title,
            platform = item.platform,
            status = item.status.name,
            rating = item.rating,
            notes = item.notes,
            coverUrl = item.coverUrl
        )
        return if (item.id == null) {
            col().add(remote).await().id
        } else {
            col().document(item.id).set(remote).await()
            item.id
        }
    }

    override suspend fun delete(id: String) {
        col().document(id).delete().await()
    }

    data class GameRemote(
        val title: String = "",
        val platform: String = "",
        val status: String = GameStatus.PLAYING.name,
        val rating: Int = 0,
        val notes: String = "",
        val coverUrl: String = ""
    )
}