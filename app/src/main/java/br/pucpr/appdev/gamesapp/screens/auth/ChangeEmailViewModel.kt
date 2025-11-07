package br.pucpr.appdev.gamesapp.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ChangeEmailViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    suspend fun changeEmail(
        newEmail: String,
        currentPassword: String
    ): Result<String> {
        return try {
            val user = auth.currentUser
            if (user == null) {
                return Result.failure(Exception("Usuário não autenticado"))
            }

            val currentEmail = user.email ?: ""

            val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)
            user.reauthenticate(credential).await()

            user.verifyBeforeUpdateEmail(newEmail).await()

            Result.success("Email de verificação enviado para $newEmail")
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                e.message?.contains("wrong-password") == true ->
                    "Senha atual incorreta"
                e.message?.contains("email-already-in-use") == true ->
                    "Este email já está em uso"
                e.message?.contains("invalid-email") == true ->
                    "Email inválido"
                e.message?.contains("network") == true ->
                    "Erro de conexão. Verifique sua internet"
                else -> "Erro: ${e.message}"
            }
            Result.failure(Exception(message))
        }
    }
}

