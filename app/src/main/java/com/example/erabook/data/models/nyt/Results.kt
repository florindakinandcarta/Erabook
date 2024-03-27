package com.example.erabook.data.models.nyt

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("bestsellers_date"           ) val bestsellersDate          : String?          = null,
    @SerializedName("published_date"             ) val publishedDate            : String?          = null,
    @SerializedName("published_date_description" ) val publishedDateDescription : String?          = null,
    @SerializedName("previous_published_date"    ) val previousPublishedDate    : String?          = null,
    @SerializedName("next_published_date"        ) val nextPublishedDate        : String?          = null,
    @SerializedName("lists"                      ) val lists                    : ArrayList<Lists> = arrayListOf()
)