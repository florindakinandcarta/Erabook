package com.example.erabook.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VolumeInfo(
    var title: String? = null,
    var authors: ArrayList<String> = arrayListOf(),
    var publisher: String? = null,
    var publishedDate: String? = null,
    var description: String? = null,
    var industryIdentifiers: ArrayList<IndustryIdentifiers> = arrayListOf(),
    var readingModes: ReadingModes? = ReadingModes(),
    var pageCount: Int? = null,
    var printType: String? = null,
    var categories: ArrayList<String> = arrayListOf(),
    var averageRating: Int? = null,
    var ratingsCount: Int? = null,
    var maturityRating: String? = null,
    var allowAnonLogging: Boolean? = null,
    var contentVersion: String? = null,
    var panelizationSummary: PanelizationSummary? = PanelizationSummary(),
    var imageLinks: ImageLinks? = ImageLinks(),
    var language: String? = null,
    var previewLink: String? = null,
    var infoLink: String? = null,
    var canonicalVolumeLink: String? = null
) : Parcelable
