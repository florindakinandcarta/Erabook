package com.example.erabook.fragments.favorite

import androidx.lifecycle.ViewModel
import com.example.erabook.data.Books
import com.example.erabook.fragments.home.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SHARED_PREFERENCES = "favorites"

class FavoriteViewModel : ViewModel() {

    fun loadFavorites(): List<Books> {
        val type = object : TypeToken<List<Books>>() {}.type
        return Gson().fromJson(
            HomeFragment.sharedPreferences.getString(SHARED_PREFERENCES, "[]") ?: "[]",
            type
        ) ?: emptyList()
    }

    fun saveFavorites(favorites: List<Books>) {
        val jsonString = Gson().toJson(favorites)
        HomeFragment.sharedPreferences.edit().putString(SHARED_PREFERENCES, jsonString).apply()
    }
}