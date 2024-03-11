package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    var kind: String? = null,
    var id: String? = null,
    var etag: String? = null,
    var selfLink: String? = null,
    var volumeInfo: VolumeInfo? = VolumeInfo(),
    var saleInfo: SaleInfo? = SaleInfo(),
    var accessInfo: AccessInfo? = AccessInfo(),
    var searchInfo: SearchInfo? = SearchInfo()
) : Parcelable
