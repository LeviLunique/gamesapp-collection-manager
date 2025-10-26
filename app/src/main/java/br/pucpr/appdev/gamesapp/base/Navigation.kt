package br.pucpr.appdev.gamesapp.base

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

class Navigation(private val navController: NavHostController) {

    @Composable
    fun BuildNavGraph() {
        NavHost(
            navController = navController,
            startDestination = Routes.ListGames.route
        ) {
            composable(Routes.ListGames.route) {
                CallScaffold(navController).CreateScreen(Routes.ListGames.route)
            }

            composable(Routes.CreateGame.route) {
                CallScaffold(navController).CreateScreen(Routes.CreateGame.route)
            }

            composable(
                "${Routes.EditGame.route}?${Constants.ARG_ID}={${Constants.ARG_ID}}",
                arguments = listOf(navArgument(Constants.ARG_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) {
                CallScaffold(navController).CreateScreen(Routes.EditGame.route)
            }
        }
    }
}