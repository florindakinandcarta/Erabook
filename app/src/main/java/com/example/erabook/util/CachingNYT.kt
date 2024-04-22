package com.example.erabook.util

import android.content.Context
import android.content.SharedPreferences
import com.example.erabook.BuildConfig
import com.example.erabook.data.models.nyt.NYTBooks
import com.google.gson.Gson

object CachingNYT {
    private const val PREF_NAME = "CachingNYT_Preferences"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun cacheNYTBooks(context: Context, nytBooks: NYTBooks) {
        val json = Gson().toJson(nytBooks)
        getSharedPreferences(context).edit().putString(BuildConfig.NYT_API_KEY, json).apply()
    }

    fun getCachedNYTBooks(context: Context): NYTBooks? {
        val json = getSharedPreferences(context).getString(BuildConfig.NYT_API_KEY, null)
        return if (json != null) {
            Gson().fromJson(json, NYTBooks::class.java)
        } else {
            null
        }
    }
}