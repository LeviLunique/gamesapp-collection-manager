package br.pucpr.appdev.gamesapp.base

sealed class Routes(val route: String) {
    data object ListGames  : Routes("listGames")
    data object CreateGame : Routes("createGame")
    data object EditGame   : Routes("editGame")

    companion object {
        fun editWithId(id: Long) = "${EditGame.route}?${Constants.Nav.ARG_ID}=$id"
    }
}