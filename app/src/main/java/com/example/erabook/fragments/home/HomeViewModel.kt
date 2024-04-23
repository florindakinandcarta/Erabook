package com.example.erabook.fragments.home

import Resource
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.models.ExceptionResponse
import com.example.erabook.data.models.nyt.NYTBooks
import com.example.erabook.data.nytService.NYTService
import com.example.erabook.util.CachingNYT
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val nytService: NYTService
) : ViewModel() {
    private val _nyt = MutableLiveData<Resource<NYTBooks>?>()
    val nyt: LiveData<Resource<NYTBooks>?> = _nyt

    fun callNYT(apiKey: String,context: Context) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = nytService.getNYTCurrentBestSellers(apiKey)
                    _nyt.postValue(Resource.Success(response))
                    CachingNYT.cacheNYTBooks(context,response)
                }
            } catch (httpException: HttpException) {
                val errorResponse = Gson().fromJson(
                    httpException.response()?.errorBody()?.string(),
                    ExceptionResponse::class.java
                )
                _nyt.postValue(Resource.Error(errorResponse?.message ?: ""))
            }catch (e: Exception){
                _nyt.postValue(Resource.Error())
            }finally {
            }
        }
    }
}