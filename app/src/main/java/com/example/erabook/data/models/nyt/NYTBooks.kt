package com.example.erabook.data.models.nyt

import com.google.gson.annotations.SerializedName

data class NYTBooks(
    @SerializedName("status"      ) val status     : String?  = null,
    @SerializedName("copyright"   ) val copyright  : String?  = null,
    @SerializedName("num_results" ) val numResults : Int?     = null,
    @SerializedName("results"     ) val results    : Results? = Results()
)