package com.example.erabook.data.googlebooks

import com.example.erabook.data.models.GoogleBooks
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {
    @GET("volumes")
    suspend fun getGoogleBooks(
        @Query("q") q: String?,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int,
    ): GoogleBooks
}