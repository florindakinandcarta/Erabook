package com.example.erabook.data.nytService

import com.example.erabook.data.models.nyt.NYTBooks
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTService {
    @GET("full-overview.json?")
    suspend fun getNYTCurrentBestSellers(
        @Query("api-key") apiKey:String?,
    ): NYTBooks
}