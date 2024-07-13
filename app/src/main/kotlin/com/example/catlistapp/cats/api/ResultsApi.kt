package com.example.catlistapp.cats.api

import com.example.catlistapp.networking.dto.ResultDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ResultsApi {

    @GET("leaderboard")
    suspend fun getAllResultsForCategory(@Query("category") category: Int): List<ResultDTO>

    @POST("leaderboard")
    suspend fun postResult(@Body obj:ResultDTO)
}