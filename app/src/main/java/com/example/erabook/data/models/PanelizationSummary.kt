package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PanelizationSummary(
    val containsEpubBubbles: Boolean? = null,
    val containsImageBubbles: Boolean? = null
) : Parcelable
