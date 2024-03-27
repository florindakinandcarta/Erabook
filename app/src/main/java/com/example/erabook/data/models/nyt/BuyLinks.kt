package com.example.erabook.data.models.nyt

import com.google.gson.annotations.SerializedName

data class BuyLinks(
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null
)