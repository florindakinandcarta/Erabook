package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pdf(
    val isAvailable: Boolean? = null,
    val downloadLink: String? = null,
    val acsTokenLink: String? = null
) : Parcelable
