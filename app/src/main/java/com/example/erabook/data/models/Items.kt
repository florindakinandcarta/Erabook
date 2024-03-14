package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    val kind: String? = null,
    val id: String? = null,
    val etag: String? = null,
    val selfLink: String? = null,
    val volumeInfo: VolumeInfo? = VolumeInfo(),
    val saleInfo: SaleInfo? = SaleInfo(),
    val accessInfo: AccessInfo? = AccessInfo(),
    val searchInfo: SearchInfo? = SearchInfo()
) : Parcelable
