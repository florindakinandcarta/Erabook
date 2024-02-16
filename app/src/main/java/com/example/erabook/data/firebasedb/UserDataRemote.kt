package com.example.erabook.data.firebasedb

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDataRemote(
    @SerializedName("userUid") var userUid: String,
    @SerializedName("userName") var userName: String,
    @SerializedName("userEmail") var userEmail: String,
//    @SerializedName("user_location") var userLocation: Coordinates?,
    @SerializedName("userMobile") var userMobile: Int,
    @SerializedName("userProfileImg") var userProfileImg: String?,
    @SerializedName("userUsername") var userUsername: String,
    @SerializedName("userBirthday") var userBirthday: Timestamp,
    @SerializedName("favoriteBooks") var favoriteBooks: ArrayList<FavoriteBooks> = arrayListOf()
)

data class Coordinates(
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("lat") var lat: Double? = null,
)

data class FavoriteBooks(
    @SerializedName("book_title") var bookTitle: String,
    @SerializedName("book_author") var bookAuthor: String,
)
