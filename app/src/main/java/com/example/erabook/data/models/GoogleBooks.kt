package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoogleBooks(
    var kind: String? = null,
    var totalItems: Int? = null,
    var items: ArrayList<Items> = arrayListOf()
) : Parcelable

