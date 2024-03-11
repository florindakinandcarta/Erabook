package com.example.erabook.data.models

import Epub
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessInfo(
    var country: String? = null,
    var viewability: String? = null,
    var embeddable: Boolean? = null,
    var publicDomain: Boolean? = null,
    var textToSpeechPermission: String? = null,
    var epub: Epub? = Epub(),
    var pdf: Pdf? = Pdf(),
    var webReaderLink: String? = null,
    var accessViewStatus: String? = null,
    var quoteSharingAllowed: Boolean? = null
) : Parcelable
