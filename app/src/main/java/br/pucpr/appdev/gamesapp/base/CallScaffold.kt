package br.pucpr.appdev.gamesapp.base

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import br.pucpr.appdev.gamesapp.screens.*

class CallScaffold(private val navController: NavController) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateScreen(screen: String): PaddingValues {
        var paddingValues by remember { mutableStateOf(PaddingValues()) }

        Scaffold(
            topBar = { TopAppBar(title = { Text("Gerenciador de Jogos") }) }
        ) { padding ->
            paddingValues = padding
            when (screen) {
                Routes.ListGames.route -> ListGamesScreen(
                    padding = padding,
                    onAdd = { navController.navigate(Routes.CreateGame.route) },
                    onEdit = { id -> navController.navigate("${Routes.EditGame.route}?${Constants.ARG_ID}=$id") }
                )

                Routes.CreateGame.route -> CreateGameScreen(
                    padding = padding,
                    onDone = { navController.popBackStack() }
                )

                Routes.EditGame.route -> EditGameScreen(
                    padding = padding,
                    onDone = { navController.popBackStack() }
                )
            }
        }
        return paddingValues
    }
}