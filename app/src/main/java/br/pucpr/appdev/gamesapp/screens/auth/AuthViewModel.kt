package br.pucpr.appdev.gamesapp.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = FirebaseAuth.getInstance()

    fun currentUser() = auth.currentUser

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }

    fun logout() { auth.signOut() }
}