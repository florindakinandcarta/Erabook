package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoogleBooks(
    val kind: String? = null,
    val totalItems: Int? = null,
    val items: ArrayList<Items> = arrayListOf()
) : Parcelable

