package br.pucpr.appdev.gamesapp.model

interface IGameRepository {
    suspend fun list(): List<GameItem>
    suspend fun get(id: String): GameItem?
    suspend fun upsert(item: GameItem): String
    suspend fun delete(id: String)
}