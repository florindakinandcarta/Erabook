package com.example.erabook.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDataRemote(
    @SerializedName("userUid") val userUid: String? = null,
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("userEmail") val userEmail: String? = null,
    @SerializedName("userLocation") val userLocation: ArrayList<String>? =arrayListOf(),
    @SerializedName("userMobile") val userMobile: String? = null,
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
