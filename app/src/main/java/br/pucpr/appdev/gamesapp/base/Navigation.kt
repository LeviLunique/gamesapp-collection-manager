package br.pucpr.appdev.gamesapp.base

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import br.pucpr.appdev.gamesapp.screens.*
import br.pucpr.appdev.gamesapp.screens.auth.ChangeEmailScreen
import br.pucpr.appdev.gamesapp.screens.auth.ChangePasswordScreen
import br.pucpr.appdev.gamesapp.screens.auth.EditProfileScreen
import br.pucpr.appdev.gamesapp.screens.auth.ForgotPasswordScreen
import br.pucpr.appdev.gamesapp.screens.auth.LoginScreen
import br.pucpr.appdev.gamesapp.screens.auth.RegisterScreen

class Navigation(private val navController: NavHostController, private val start: String) {

    @Composable
    fun BuildNavGraph() {
        NavHost(
            navController = navController,
            startDestination = start
        ) {
            composable(Routes.Login.route) {
                LoginScreen(
                    onGoRegister = { navController.navigate(Routes.Register.route) },
                    onForgotPassword = { navController.navigate(Routes.ForgotPassword.route) },
                    onLoggedIn = {
                        navController.navigate(Routes.ListGames.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.Register.route) {
                RegisterScreen(
                    onGoLogin = { navController.popBackStack() },
                    onRegistered = {
                        navController.navigate(Routes.ListGames.route) {
                            popUpTo(Routes.Register.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.ListGames.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    ListGamesScreen(
                        padding = padding,
                        onAdd = { navController.navigate(Routes.CreateGame.route) },
                        onEdit = { id -> navController.navigate(Routes.editWithId(id)) },
                        onEditProfile = { navController.navigate(Routes.EditProfile.route) },
                        onLogout = {
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.ListGames.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
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
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments
                    ?.getString(Constants.Nav.ARG_ID)
                    ?.takeIf { it.isNotBlank() }

                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    EditGameScreen(
                        padding = padding,
                        onDone = { navController.popBackStack() },
                        gameId = gameId
                    )
                }
            }

            composable(Routes.EditProfile.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    EditProfileScreen(
                        padding = padding,
                        onChangeEmail = { navController.navigate(Routes.ChangeEmail.route) },
                        onChangePassword = { navController.navigate(Routes.ChangePassword.route) },
                        onAccountDeleted = {
                            navController.navigate(Routes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onDone = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.ChangeEmail.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    ChangeEmailScreen(
                        padding = padding,
                        onDone = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.ChangePassword.route) {
                CallScaffold(topTitle = "Gerenciador de Jogos") { padding ->
                    ChangePasswordScreen(
                        padding = padding,
                        onDone = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}