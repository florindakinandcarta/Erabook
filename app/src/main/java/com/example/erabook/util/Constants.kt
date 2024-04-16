package com.example.erabook.util

import com.example.erabook.R
import com.example.erabook.data.TabNames
import com.example.erabook.data.TabNamesItem

const val ARG_TAB_NAME = "tab_name"

val TAB_NAMES = listOf(
    TabNamesItem(TabNames.FOR_YOU, R.string.recommended_for_you),
    TabNamesItem(TabNames.NEW_YORK_BEST_SELLERS, R.string.new_york_best_sellers),
)