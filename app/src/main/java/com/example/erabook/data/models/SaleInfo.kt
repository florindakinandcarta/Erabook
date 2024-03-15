package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaleInfo(
    val country     : String?  = null,
    val saleability : String?  = null,
    val isEbook     : Boolean? = null
):Parcelable
