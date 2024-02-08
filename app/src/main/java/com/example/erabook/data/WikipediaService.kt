package com.example.erabook.data

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface WikipediaService {
    @GET
    suspend fun getWikipediaPage(@Url url: String): ResponseBody
}