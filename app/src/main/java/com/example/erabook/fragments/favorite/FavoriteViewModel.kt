package com.example.erabook.fragments.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.models.ImageLinks
import com.example.erabook.data.models.VolumeInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val SHARED_PREFERENCES = "favorites"

class FavoriteViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _listOfBooks = MutableLiveData<List<VolumeInfo>?>()
    val listOfBooks: LiveData<List<VolumeInfo>?> = _listOfBooks
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    fun fetchFavoriteBooks(userEmail: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _loading.postValue(true)
                    val querySnapshot = db.collection("erabook-users")
                        .whereEqualTo("userEmail", userEmail)
                        .get()
                        .await()

                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val booksList = document["favoriteBooks"] as List<Map<String, Any>>?

                        val volumeInfoList = booksList?.mapNotNull { bookMap ->
                            val volumeInfoMap = bookMap["volumeInfo"] as? Map<String, Any>
                            VolumeInfo(
                                title = volumeInfoMap?.get("title") as? String,
                                authors = (volumeInfoMap?.get("authors") as? ArrayList<String>)
                                    ?: ArrayList(),
                                publishedDate = volumeInfoMap?.get("publishedDate") as? String,
                                description = volumeInfoMap?.get("description") as? String,
                                pageCount = volumeInfoMap?.get("pageCount") as? Int,
                                categories = (volumeInfoMap?.get("categories") as? ArrayList<String>)
                                    ?: ArrayList(),
                                maturityRating = volumeInfoMap?.get("maturityRating") as? String,
                                allowAnonLogging = volumeInfoMap?.get("allowAnonLogging") as? Boolean,
                                contentVersion = volumeInfoMap?.get("contentVersion") as? String,
                                language = volumeInfoMap?.get("language") as? String,
                                previewLink = volumeInfoMap?.get("previewLink") as? String,
                                imageLinks = (volumeInfoMap?.get("imageLinks") as? Map<String, Any>)?.let { imageLinksMap ->
                                    ImageLinks(
                                        smallThumbnail = imageLinksMap["smallThumbnail"] as? String,
                                        thumbnail = imageLinksMap["thumbnail"] as? String
                                    )
                                },
                                infoLink = volumeInfoMap?.get("infoLink") as? String,
                                canonicalVolumeLink = volumeInfoMap?.get("canonicalVolumeLink") as? String
                            )
                        }
                        _listOfBooks.postValue(volumeInfoList)
                    } else {
                        _loading.postValue(false)
                        _listOfBooks.postValue(null)
                    }
                } catch (e: Exception) {
                    _loading.postValue(false)
                    println("Error fetching books: $e")
                    _listOfBooks.postValue(null)
                }
            }
        }
    }
}