package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PanelizationSummary(
    var containsEpubBubbles: Boolean? = null,
    var containsImageBubbles: Boolean? = null
) : Parcelable
