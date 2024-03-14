package com.example.erabook.di

import com.example.erabook.data.googlebooks.GoogleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BooksModule {
    private const val BASE_URL  = "https://www.googleapis.com/books/v1/"
    @Provides
    @Singleton
    fun getDataFromGoogleApi() : GoogleApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleApi::class.java)
    }



}