package com.example.erabook.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.erabook.data.repo.WikipediaRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class BookDetailsViewModel(private val url: String) : ViewModel() {

    private val _firstParagraph = MutableLiveData<String>()
    val firstParagraph: LiveData<String>
        get() = _firstParagraph

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val responseBody = WikipediaRepo().wikipediaService.getWikipediaPage(url)
                    val firstParagraph = parseHtmlForFirstParagraph(responseBody.string())
                    _firstParagraph.postValue(firstParagraph)
                } catch (e: Exception) {
                    _firstParagraph.postValue("Failed to fetch data!!!")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun parseHtmlForFirstParagraph(html: String?): String {
        val doc = html?.let { Jsoup.parse(it) }
        val paragraphs = doc?.select("p")?.take(4)
        return paragraphs?.joinToString(separator = "\n") { it.text() }
            ?: "Paragraphs not found!!!"
    }
}


class BookDetailsViewModelFactory(private val url: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailsViewModel::class.java)) {
            return BookDetailsViewModel(url) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}