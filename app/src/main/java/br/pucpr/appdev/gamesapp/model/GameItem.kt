package br.pucpr.appdev.gamesapp.model

data class GameItem(
    val id: String? = null,
    val title: String = "",
    val platform: String = "",
    val status: GameStatus = GameStatus.BACKLOG,
    val rating: Int = 0,
    val notes: String = "",
    val coverUrl : String = ""
)