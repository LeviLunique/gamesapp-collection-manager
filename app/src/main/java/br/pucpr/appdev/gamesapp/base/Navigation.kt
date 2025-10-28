package br.pucpr.appdev.gamesapp.base

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import br.pucpr.appdev.gamesapp.screens.*

class Navigation(private val navController: NavHostController) {

    @Composable
    fun BuildNavGraph() {
        NavHost(
            navController = navController,
            startDestination = Routes.ListGames.route
        ) {
            composable(Routes.ListGames.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    ListGamesScreen(
                        padding = padding,
                        onAdd = { navController.navigate(Routes.CreateGame.route) },
                        onEdit = { id -> navController.navigate(Routes.editWithId(id)) }
                    )
                }
            }

            composable(Routes.CreateGame.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    CreateGameScreen(
                        padding = padding,
                        onDone = { navController.popBackStack() }
                    )
                }
            }

            composable(
                route = "${Routes.EditGame.route}?${Constants.Nav.ARG_ID}={${Constants.Nav.ARG_ID}}",
                arguments = listOf(
                    navArgument(Constants.Nav.ARG_ID) {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments
                    ?.getLong(Constants.Nav.ARG_ID)
                    ?.takeIf { it > 0L }

                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    EditGameScreen(
                        padding = padding,
                        onDone = { navController.popBackStack() },
                        gameId = gameId
                    )
                }
            }
        }
    }
}