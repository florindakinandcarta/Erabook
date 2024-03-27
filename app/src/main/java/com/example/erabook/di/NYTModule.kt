package com.example.erabook.di

import com.example.erabook.data.nytService.NYTService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NYTModule {
    private const val BASE_URL = "https://api.nytimes.com/svc/books/v3/lists/"
    @Provides
    @Singleton
    fun getCurrentNYTBooks(): NYTService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NYTService::class.java)
    }
}