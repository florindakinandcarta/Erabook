package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IndustryIdentifiers(
    var type: String? = null,
    var identifier: String? = null
) : Parcelable