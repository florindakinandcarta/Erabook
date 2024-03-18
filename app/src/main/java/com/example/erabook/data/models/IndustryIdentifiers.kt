package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IndustryIdentifiers(
    val type: String? = null,
    val identifier: String? = null
) : Parcelable