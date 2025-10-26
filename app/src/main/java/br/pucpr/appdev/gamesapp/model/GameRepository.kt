package br.pucpr.appdev.gamesapp.model

class GameRepository(private val dao: GameDao) {
    suspend fun getAll() = dao.getAll()
    suspend fun insert(game: GameEntity) = dao.insert(game)
    suspend fun delete(game: GameEntity) = dao.delete(game)
}