package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.data.models.Books
import com.example.erabook.databinding.ItemFavoriteBookBinding


class FavoriteAdapter : ListAdapter<Books, FavoriteAdapter.ViewHolder>(HomeAdapterDiffCallBack()) {

    inner class ViewHolder(private val binding: ItemFavoriteBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(books: Books) {
            binding.apply {
                book = books
                favoriteBookItem.setOnClickListener {
                    removeItem(adapterPosition)
                }
                this.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun removeItem(position: Int) {
        submitList(currentList.toMutableList().apply {
            removeAt(position)
        })
    }
}