package com.example.erabook.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.OSMService
import com.example.erabook.data.models.ExceptionResponse
import com.example.erabook.data.models.OSM
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val osmService: OSMService) : ViewModel() {
    private val _osm = MutableLiveData<OSM?>()
    val osm: LiveData<OSM?> = _osm
    fun getLocationPlace(latitude: Double?, longitude:Double?){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val response = osmService.getLocationName(latitude.toString(),longitude.toString())
                    _osm.postValue(response)
                }
            }catch (httpException: HttpException) {
                val errorResponse = Gson().fromJson(
                    httpException.response()?.errorBody()?.string(),
                    ExceptionResponse::class.java
                )
                print(errorResponse?.message)
            }catch (e: Exception){
                print(e.message)
            }finally {
            }
        }
    }
}