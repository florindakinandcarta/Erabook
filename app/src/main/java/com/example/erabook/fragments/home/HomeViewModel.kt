package com.example.erabook.fragments.home

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.models.ExceptionResponse
import com.example.erabook.data.models.nyt.NYTBooks
import com.example.erabook.data.nytService.NYTService
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import retrofit2.HttpException

@HiltViewModel
class HomeViewModel@Inject constructor( private val nytService: NYTService): ViewModel() {
    private val _nyt = MutableLiveData<Resource<NYTBooks>?>()
    val nyt: LiveData<Resource<NYTBooks>?> = _nyt


    fun callNYT(apiKey: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = nytService.getNYTCurrentBestSellers(apiKey)
                    _nyt.postValue(Resource.Success(response))
                }
            }catch (httpException: HttpException){
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