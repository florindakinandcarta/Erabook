package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaleInfo(
    var country     : String?  = null,
    var saleability : String?  = null,
    var isEbook     : Boolean? = null
):Parcelable
