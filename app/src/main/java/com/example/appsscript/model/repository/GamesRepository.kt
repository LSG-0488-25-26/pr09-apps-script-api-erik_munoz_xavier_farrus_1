package com.example.appsscript.model.repository

import com.example.appsscript.BuildConfig
import com.example.appsscript.model.api.RetrofitInstance
import com.example.appsscript.model.data.GetResponse
import com.example.appsscript.model.data.Game
import com.example.appsscript.model.data.PostRequest
import com.example.appsscript.model.data.PostResponse

class GamesRepository {
    private val api = RetrofitInstance.api
    private val apiKey = BuildConfig.API_KEY

    suspend fun getAllGames(): GetResponse<List<Game>> = api.getAllGames(apiKey)

    suspend fun getFreeGames(): GetResponse<List<Game>> = api.getFreeGames(apiKey)

    suspend fun getValveGames(): GetResponse<List<Game>> = api.getValveGames(apiKey)

    suspend fun insertGame(game: PostRequest): PostResponse = api.insertGame(
        game.copy(apiKey = apiKey)
    )
}
