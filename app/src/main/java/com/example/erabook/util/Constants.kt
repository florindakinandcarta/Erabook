package com.example.erabook.util

import com.example.erabook.R
import com.example.erabook.data.TabNames
import com.example.erabook.data.TabNamesItem

const val ARG_TAB_NAME = "tab_name"

val TAB_NAMES = listOf(
    TabNamesItem(TabNames.RECOMMENDED_FOR_YOU, R.string.recommended_for_you),
    TabNamesItem(TabNames.TOP_100, R.string.top_100),
    TabNamesItem(TabNames.NEW_YORK_BEST_SELLERS, R.string.new_york_best_sellers),
    TabNamesItem(TabNames.MY_GENRES, R.string.my_genres),
    TabNamesItem(TabNames.STATISTICS, R.string.statistics)
)