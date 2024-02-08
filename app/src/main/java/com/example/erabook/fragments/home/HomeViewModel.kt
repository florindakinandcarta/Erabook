package com.example.erabook.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.Books
import com.example.erabook.data.DataLocal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch


private const val SHARED_PREFERENCES = "favorites"


class HomeViewModel(private val jsonFile: String) : ViewModel() {

    private val _booksData = MutableLiveData<List<Books>>().apply {
        viewModelScope.launch {
            try {
                val bookData = Gson().fromJson(jsonFile, DataLocal::class.java)
                value = bookData.booksList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
    val booksList: LiveData<List<Books>> get() = _booksData


    fun addToFavorites(book: Books) {
        val favorites = getFavorites()
        favorites.add(book)
        saveFavorites(favorites)
    }

    private fun getFavorites(): MutableSet<Books> {
        val type = object : TypeToken<MutableSet<Books>>() {}.type
        return Gson().fromJson(
            HomeFragment.sharedPreferences.getString(SHARED_PREFERENCES, "[]") ?: "[]", type
        )
            ?: mutableSetOf()
    }

    private fun saveFavorites(favorites: Set<Books>) {
        val editor = HomeFragment.sharedPreferences.edit()
        editor.putString(SHARED_PREFERENCES, Gson().toJson(favorites))
        editor.apply()
    }

}

class HomeViewModelFactory(private val filePath: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(filePath) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}