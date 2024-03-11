package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pdf(
    var isAvailable: Boolean? = null,
    var acsTokenLink: String? = null
) : Parcelable
