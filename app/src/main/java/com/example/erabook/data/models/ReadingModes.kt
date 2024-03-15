package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingModes(
    val text  : Boolean? = null,
    val image : Boolean? = null
):Parcelable