package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageLinks(
    var smallThumbnail: String? = null,
    var thumbnail: String? = null
) : Parcelable
