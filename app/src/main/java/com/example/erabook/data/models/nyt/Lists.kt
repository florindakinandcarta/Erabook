package com.example.erabook.data.models.nyt

import com.google.gson.annotations.SerializedName

data class Lists(
    @SerializedName("list_id"           ) val listId          : Int?             = null,
    @SerializedName("list_name"         ) val listName        : String?          = null,
    @SerializedName("list_name_encoded" ) val listNameEncoded : String?          = null,
    @SerializedName("display_name"      ) val displayName     : String?          = null,
    @SerializedName("updated"           ) val updated         : String?          = null,
    @SerializedName("list_image"        ) val listImage       : String?          = null,
    @SerializedName("list_image_width"  ) val listImageWidth  : String?          = null,
    @SerializedName("list_image_height" ) val listImageHeight : String?          = null,
    @SerializedName("books"             ) val books           : ArrayList<Books> = arrayListOf()
)