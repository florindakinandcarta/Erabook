package com.example.erabook.data.repo

import com.example.erabook.data.WikipediaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WikipediaRepo {
     val wikipediaService: WikipediaService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/wiki/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        wikipediaService = retrofit.create(WikipediaService::class.java)
    }
}