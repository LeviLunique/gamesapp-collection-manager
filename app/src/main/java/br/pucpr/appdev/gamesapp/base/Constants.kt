package br.pucpr.appdev.gamesapp.base

class Constants private constructor() {
    companion object {
        const val DB_NAME = "games_db"
        const val TABLE_GAMES = "games"
        const val ARG_ID = "id"

        const val STATUS_BACKLOG = "BACKLOG"
        const val STATUS_PLAYING = "PLAYING"
        const val STATUS_DONE = "DONE"
    }
}