package br.pucpr.appdev.gamesapp.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("user-not-found") == true ->
                    "Nenhuma conta encontrada com este email"
                e.message?.contains("invalid-email") == true ->
                    "Email inválido"
                e.message?.contains("network") == true ->
                    "Erro de conexão. Verifique sua internet"
                else -> "Erro ao enviar email: ${e.message}"
            }
            Result.failure(Exception(message))
        }
    }
}

