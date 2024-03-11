package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchInfo(
    var textSnippet: String? = null
) : Parcelable
