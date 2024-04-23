package com.example.erabook.data

import com.example.erabook.data.models.OSM
import retrofit2.http.GET
import retrofit2.http.Query

interface OSMService {
    @GET("reverse?format=json")
    suspend fun getLocationName(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("zoom") zoom: Int = 18,
        @Query("addressdetails") addressdetails: Int = 1,
        @Query("accept-language") acceptLanguage: String = "en",

    ): OSM
}