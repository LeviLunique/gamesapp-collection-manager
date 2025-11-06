package br.pucpr.appdev.gamesapp.model

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadCover(tempDocId: String?, localUri: Uri): String {
        val uid = requireNotNull(auth.currentUser?.uid) { "Usuário não autenticado" }
        val docId = tempDocId ?: UUID.randomUUID().toString()
        val ref = storage.getReference("users/$uid/covers/$docId.jpg")
        ref.putFile(localUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteCoverIfAny(coverUrl: String) {
        if (coverUrl.isBlank()) return
        try {
            storage.getReferenceFromUrl(coverUrl).delete().await()
        } catch (_: Exception) {
        }
    }
}