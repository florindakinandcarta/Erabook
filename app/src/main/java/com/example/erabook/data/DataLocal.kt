package com.example.erabook.data

import com.google.gson.annotations.SerializedName

data class DataLocal(
    @SerializedName("books_list") var booksList: ArrayList<Books> = arrayListOf()
)

data class Books(
    @SerializedName("author") var author: String,
    @SerializedName("country") var country: String?,
    @SerializedName("imageLink") var imageLink: String?,
    @SerializedName("language") var language: String?,
    @SerializedName("link") var link: String?,
    @SerializedName("pages") var pages: Int?,
    @SerializedName("title") var title: String,
    @SerializedName("year") var year: Int?,
    @SerializedName("isFavorite") var isFavorite: Boolean
)