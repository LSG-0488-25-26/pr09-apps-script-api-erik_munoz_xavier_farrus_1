package com.example.appsscript.model.api

import com.example.appsscript.model.data.GetResponse
import com.example.appsscript.model.data.Game
import com.example.appsscript.model.data.PostRequest
import com.example.appsscript.model.data.PostResponse
import retrofit2.http.*

interface ApiInterface {

    @GET("exec")
    suspend fun getAllGames(
        @Query("apiKey") apiKey: String,
        @Query("type") type: String = "todos"
    ): GetResponse<List<Game>>

    @GET("exec")
    suspend fun getFreeGames(
        @Query("apiKey") apiKey: String,
        @Query("type") type: String = "gratis"
    ): GetResponse<List<Game>>

    @GET("exec")
    suspend fun getValveGames(
        @Query("apiKey") apiKey: String,
        @Query("type") type: String = "valve"
    ): GetResponse<List<Game>>

    @POST("exec")
    suspend fun insertGame(
        @Body game: PostRequest
    ): PostResponse
}
