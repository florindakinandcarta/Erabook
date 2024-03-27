package com.example.erabook.data

data class TabNamesItem(
    val type: TabNames,
    val tabName: Int,
)
enum class TabNames {
    RECOMMENDED_FOR_YOU,
    TOP_100,
    NEW_YORK_BEST_SELLERS,
    MY_GENRES,
    STATISTICS
}