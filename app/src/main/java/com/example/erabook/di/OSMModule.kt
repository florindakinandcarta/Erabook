package com.example.erabook.di

import com.example.erabook.data.OSMService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OSMModule {
    private const val OSM_URL = "https://nominatim.openstreetmap.org/"
    @Provides
    @Singleton
    fun getLocationPlace(): OSMService{
        return Retrofit.Builder()
            .baseUrl(OSM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OSMService::class.java)
    }
}