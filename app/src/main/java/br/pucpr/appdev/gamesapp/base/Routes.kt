package br.pucpr.appdev.gamesapp.base

sealed class Routes(val route: String) {
    data object Login          : Routes("login")
    data object Register       : Routes("register")
    data object ForgotPassword : Routes("forgotPassword")
    data object ListGames      : Routes("listGames")
    data object CreateGame     : Routes("createGame")
    data object EditGame       : Routes("editGame")
    data object EditProfile    : Routes("editProfile")
    data object ChangeEmail    : Routes("changeEmail")
    data object ChangePassword : Routes("changePassword")

    companion object {
        fun editWithId(id: String) = "${EditGame.route}?${Constants.Nav.ARG_ID}=$id"
    }
}