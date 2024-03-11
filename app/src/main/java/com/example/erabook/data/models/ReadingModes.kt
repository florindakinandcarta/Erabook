package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingModes(
    var text  : Boolean? = null,
    var image : Boolean? = null
):Parcelable