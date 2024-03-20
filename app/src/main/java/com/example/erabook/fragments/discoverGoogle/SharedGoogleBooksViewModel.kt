package com.example.erabook.fragments.discoverGoogle

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.googlebooks.GoogleApi
import com.example.erabook.data.models.ExceptionResponse
import com.example.erabook.data.models.GoogleBooks
import com.example.erabook.data.models.Items
import com.example.erabook.data.models.VolumeInfo
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class SharedGoogleBooksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val googleBooksApi: GoogleApi
) : ViewModel() {

    private val db = Firebase.firestore
    private val _response_books = MutableLiveData<Resource<GoogleBooks>?>()
    val response_books: LiveData<Resource<GoogleBooks>?> = _response_books
    private val _loading_books = MutableLiveData<Boolean>()
    val loading_books: LiveData<Boolean> = _loading_books
    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    fun fetchBooks(queryBookName: String?, loadMore: Int) {
        _loading_books.value = true
        viewModelScope.launch {
            try {
                val response =
                    googleBooksApi.getGoogleBooks(queryBookName, maxResults = loadMore + 20)
                _response_books.value = Resource.Success(response)
            } catch (httpException: HttpException) {
                val errorResponse = Gson().fromJson(
                    httpException.response()?.errorBody()?.string(),
                    ExceptionResponse::class.java
                )
                _response_books.value = Resource.Error(errorResponse?.message ?: "")
            } catch (e: Exception) {
                _response_books.value = Resource.Error()
            } finally {
                _loading_books.value = false
            }
        }
    }

    fun fetchBooksWithISBN(isbnValue: String) {
        _loading_books.value = true
        viewModelScope.launch {
            try {
                val response = googleBooksApi.getGoogleBooksWithISBN(isbnValue)
                if (response.totalItems == 0) {
                    _response_books.value = Resource.Error("No items found for ISBN: $isbnValue")
                } else {
                    _response_books.value = Resource.Success(response)
                }
            } catch (httpException: HttpException) {
                val errorResponse = Gson().fromJson(
                    httpException.response()?.errorBody()?.string(),
                    ExceptionResponse::class.java
                )
                _response_books.value = Resource.Error(errorResponse?.message ?: "")
            } catch (e: Exception) {
                _response_books.value = Resource.Error()
            } finally {
                _loading_books.value = false
            }
        }
    }
    fun saveBookToDB(bookItem: Items?, userEmail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val userQuerySnapshot = db.collection("erabook-users")
                        .whereEqualTo("userEmail", userEmail)
                        .get()
                        .await()
                    val batch = db.batch()
                    for (document in userQuerySnapshot.documents) {
                        val documentRef =
                            db.collection("erabook-users").document(document.id)
                        val updatedUsersFavoriteBooks = mutableListOf<Map<String, VolumeInfo?>>()
                        val currentFavoriteBooks =
                            document.get("favoriteBooks") as? List<Map<String, VolumeInfo?>>
                        if (currentFavoriteBooks != null) {
                            updatedUsersFavoriteBooks.addAll(currentFavoriteBooks)
                        }
                        updatedUsersFavoriteBooks.add(mapOf("volumeInfo" to bookItem?.volumeInfo))
                        val userUpdatedData = mapOf<String, Any>(
                            "favoriteBooks" to updatedUsersFavoriteBooks
                        )
                        batch.set(
                            documentRef,
                            userUpdatedData,
                            SetOptions.merge()
                        )
                    }
                    batch.commit().addOnSuccessListener {
                        _isSaved.value = true
                    }
                        .addOnFailureListener {
                          _isSaved.value = false
                        }
                } catch (e: Exception) {
                    _isSaved.value = false
                    println("Something happened here: $e")
                }
            }
        }
    }
}