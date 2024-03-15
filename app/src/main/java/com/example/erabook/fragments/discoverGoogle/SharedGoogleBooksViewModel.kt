package com.example.erabook.fragments.discoverGoogle

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.erabook.data.models.GoogleBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedGoogleBooksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _response_books = MutableLiveData<Resource<GoogleBooks>>()
    val response_books: LiveData<Resource<GoogleBooks>> = _response_books
    private val _loading_books = MutableLiveData<Boolean>()
    val loading_books: LiveData<Boolean> = _loading_books


    fun setResponseBooks(books: Resource<GoogleBooks>) {
        _loading_books.value = true
        _response_books.value = books
        if (books.data != null) {
            _loading_books.value = false
        }

    }
}