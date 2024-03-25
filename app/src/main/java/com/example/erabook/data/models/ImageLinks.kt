package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
) : Parcelable
