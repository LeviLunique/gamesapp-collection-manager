package br.pucpr.appdev.gamesapp.model

import android.net.Uri
import br.pucpr.appdev.gamesapp.base.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadCover(tempDocId: String?, localUri: Uri): String {
        return try {
            val uid = requireNotNull(auth.currentUser?.uid) { "Usuário não autenticado" }
            val docId = tempDocId ?: UUID.randomUUID().toString()
            val ref = storage.getReference(Constants.Firebase.userCoverFilePath(uid, docId))

            val uploadTask = ref.putFile(localUri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            downloadUrl
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteCoverIfAny(coverUrl: String) {
        if (coverUrl.isBlank()) return
        try {
            storage.getReferenceFromUrl(coverUrl).delete().await()
        } catch (_: Exception) {
        }
    }
}