package com.example.erabook.data

data class TabNamesItem(
    val type: TabNames,
    val tabName: Int,
)
enum class TabNames {
    FOR_YOU,
    NEW_YORK_BEST_SELLERS,
}