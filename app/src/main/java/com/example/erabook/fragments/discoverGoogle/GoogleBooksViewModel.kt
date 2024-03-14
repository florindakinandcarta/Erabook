package com.example.erabook.fragments.discoverGoogle

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.googlebooks.GoogleApi
import com.example.erabook.data.models.ExceptionResponse
import com.example.erabook.data.models.GoogleBooks
import com.google.android.play.integrity.internal.q
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class GoogleBooksViewModel @Inject constructor(
    private val googleBooksApi: GoogleApi
) : ViewModel() {
    private val _loading_books = MutableLiveData<Boolean>()
    val loading_books: LiveData<Boolean> = _loading_books

    private val _response_books = MutableLiveData<Resource<GoogleBooks>>()
    val response_books: LiveData<Resource<GoogleBooks>> = _response_books
    fun fetchBooks(queryBookName: String?, loadMore: Int) {
        _loading_books.value = true
        viewModelScope.launch {
            try {
                val response = googleBooksApi.getGoogleBooks(queryBookName, maxResults = loadMore + 20)
                _response_books.value = Resource.Success(response)
            } catch (httpException: HttpException) {
                val errorResponse = Gson().fromJson(
                    httpException.response()?.errorBody()?.string(),
                    ExceptionResponse::class.java
                )
                _response_books.value = Resource.Error(errorResponse?.message ?: "")
            } catch (e: Exception) {
                println("this is the error: $e")
                _response_books.value = Resource.Error()
            } finally {
                _loading_books.value = false
            }
        }
    }

}