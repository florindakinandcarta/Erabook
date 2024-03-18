package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VolumeInfo(
    val title: String? = null,
    val authors: ArrayList<String> = arrayListOf(),
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val industryIdentifiers: ArrayList<IndustryIdentifiers> = arrayListOf(),
    val readingModes: ReadingModes? = ReadingModes(),
    val pageCount: Int? = null,
    val printType: String? = null,
    val categories: ArrayList<String> = arrayListOf(),
    val averageRating: Double? = null,
    val ratingsCount: Int? = null,
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val panelizationSummary: PanelizationSummary? = PanelizationSummary(),
    val imageLinks: ImageLinks? = ImageLinks(),
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
) : Parcelable
