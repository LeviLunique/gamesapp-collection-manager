package br.pucpr.appdev.gamesapp.model

import androidx.room.TypeConverter

enum class GameStatus { BACKLOG, PLAYING, DONE }

class Converters {
    @TypeConverter fun toStatus(value: String) = GameStatus.valueOf(value)
    @TypeConverter fun fromStatus(status: GameStatus) = status.name
}