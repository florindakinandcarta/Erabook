package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.R
import com.example.erabook.data.Books
import com.example.erabook.databinding.ItemSearchBinding

class SearchAdapter : ListAdapter<Books, SearchAdapter.ViewHolder>(HomeAdapterDiffCallBack()) {

    private var originalList: List<Books> = listOf()

    inner class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(books: Books) {
            binding.apply {
                book = books
                println(books.author)
                this.executePendingBindings()
                itemView.setOnClickListener {
                    val bundleBookData = bundleOf(
                        Pair("imageLink", books.imageLink),
                        Pair("author", books.author),
                        Pair("country", books.country),
                        Pair("language", books.language),
                        Pair("title", books.title),
                        Pair("pages", books.pages),
                        Pair("year", books.year),
                        Pair("link", books.link)
                    )
                    itemView.findNavController().navigate(
                        R.id.searchToDetails,
                        bundleBookData
                    )
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Books> = mutableListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(originalList)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in originalList) {
                        if (item.title.lowercase()
                                .contains(filterPattern) || item.author.lowercase()
                                .contains(filterPattern)
                        ) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<Books>?)
            }
        }
    }

    fun setOriginalList(list: List<Books>) {
        originalList = list
        submitList(list)
    }

}