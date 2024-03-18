package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchInfo(
    val textSnippet: String? = null
) : Parcelable
