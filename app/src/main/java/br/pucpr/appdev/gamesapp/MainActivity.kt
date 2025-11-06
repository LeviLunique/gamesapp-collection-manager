package br.pucpr.appdev.gamesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import br.pucpr.appdev.gamesapp.base.Navigation
import br.pucpr.appdev.gamesapp.base.Routes
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val start = if (FirebaseAuth.getInstance().currentUser != null)
            Routes.ListGames.route
        else
            Routes.Login.route

        setContent {
            val navController = rememberNavController()
            val nav = Navigation(navController, start)
            nav.BuildNavGraph()
        }
    }
}