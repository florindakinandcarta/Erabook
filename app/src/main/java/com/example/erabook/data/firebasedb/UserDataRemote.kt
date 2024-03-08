package com.example.erabook.data.firebasedb

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDataRemote(
    @SerializedName("userUid") val userUid: String? = null,
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("userEmail") val userEmail: String? = null,
//    @SerializedName("user_location") val userLocation: Coordinates?,
    @SerializedName("userMobile") val userMobile: Int? = null,
    @SerializedName("userProfileImg") val userProfileImg: String? = null,
    @SerializedName("userUsername") val userUsername: String? = null,
    @SerializedName("userBirthday") val userBirthday: Date? = null,
    @SerializedName("favoriteBooks") val favoriteBooks: ArrayList<FavoriteBooks> = arrayListOf()
)

data class Coordinates(
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("lat") val lat: Double? = null,
)

data class FavoriteBooks(
    @SerializedName("book_title") val bookTitle: String? = null,
    @SerializedName("book_author") val bookAuthor: String? = null,
)
