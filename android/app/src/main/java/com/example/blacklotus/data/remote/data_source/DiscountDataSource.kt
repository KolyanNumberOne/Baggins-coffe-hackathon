package com.example.blacklotus.data.remote.data_source

import com.example.blacklotus.data.models.Reccomendation
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DiscountDataSource {
    @GET("recommendations")
    @Headers("Content-Type: application/json")
    suspend fun loadRecommendations(
        @Query("id") id: Int
    ): List<Reccomendation>
}