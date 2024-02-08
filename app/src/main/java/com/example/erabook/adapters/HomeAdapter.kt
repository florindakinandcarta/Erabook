package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.R
import com.example.erabook.data.Books
import com.example.erabook.databinding.HomeBookItemBinding


class HomeAdapter(
    private val onItemClickListener: (Books) -> Unit
) :
    ListAdapter<Books, HomeAdapter.ViewHolder>(HomeAdapterDiffCallBack()) {


    inner class ViewHolder(private val binding: HomeBookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(books: Books) {
            binding.apply {
                book = books
                markFavorite.setOnClickListener {
                    onItemClickListener.invoke(books)
                    markFavorite.setImageResource(R.drawable.favorite)
                    Toast.makeText(
                        binding.root.context,
                        "${books.title} has been added to favorites.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                        R.id.homeToDetails,
                        bundleBookData
                    )
                }

                this.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeBookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
