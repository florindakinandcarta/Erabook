package com.example.erabook.data.firebasedb

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDataRemote(
    @SerializedName("userUid") var userUid: String? = null,
    @SerializedName("userName") var userName: String? = null,
    @SerializedName("userEmail") var userEmail: String? = null,
//    @SerializedName("user_location") var userLocation: Coordinates?,
    @SerializedName("userMobile") var userMobile: Int? = null,
    @SerializedName("userProfileImg") var userProfileImg: String? = null,
    @SerializedName("userUsername") var userUsername: String? = null,
    @SerializedName("userBirthday") var userBirthday: Date? = null,
    @SerializedName("favoriteBooks") var favoriteBooks: ArrayList<FavoriteBooks> = arrayListOf()
)

data class Coordinates(
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("lat") var lat: Double? = null,
)

data class FavoriteBooks(
    @SerializedName("book_title") var bookTitle: String? = null,
    @SerializedName("book_author") var bookAuthor: String? = null,
)
