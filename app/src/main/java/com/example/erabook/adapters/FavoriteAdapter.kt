package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.data.models.VolumeInfo
import com.example.erabook.databinding.ItemFavoriteBookBinding
import com.example.erabook.util.loadImageFromUrl


class FavoriteAdapter :
    ListAdapter<VolumeInfo, FavoriteAdapter.ViewHolder>(FavoriteAdapterDiffCallBack()) {


    inner class ViewHolder(private val binding: ItemFavoriteBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(books: VolumeInfo) {
            binding.apply {
                bookTitle.text = books.title
                println(books.title)
                bookAuthor.text = books.authors.get(0)
                bookImage.loadImageFromUrl(books.imageLinks?.thumbnail)
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

class FavoriteAdapterDiffCallBack : DiffUtil.ItemCallback<VolumeInfo>() {
    override fun areItemsTheSame(oldItem: VolumeInfo, newItem: VolumeInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: VolumeInfo, newItem:VolumeInfo): Boolean {
        return oldItem == newItem
    }
}
