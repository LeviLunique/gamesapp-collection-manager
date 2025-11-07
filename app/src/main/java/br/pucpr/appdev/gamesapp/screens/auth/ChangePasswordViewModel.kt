package br.pucpr.appdev.gamesapp.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ChangePasswordViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Result<String> {
        return try {
            val user = auth.currentUser
            if (user == null || user.email == null) {
                return Result.failure(Exception("Usuário não autenticado"))
            }

            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()

            user.updatePassword(newPassword).await()

            Result.success("Senha alterada com sucesso!")
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                e.message?.contains("wrong-password") == true ->
                    "Senha atual incorreta"
                e.message?.contains("network") == true ->
                    "Erro de conexão. Verifique sua internet"
                e.message?.contains("weak-password") == true ->
                    "Senha muito fraca. Tente uma senha mais forte"
                else -> "Erro: ${e.message}"
            }
            Result.failure(Exception(message))
        }
    }
}

