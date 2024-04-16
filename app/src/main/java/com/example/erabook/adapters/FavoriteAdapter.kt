package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.R
import com.example.erabook.data.models.VolumeInfo
import com.example.erabook.databinding.ItemFavoriteBookBinding
import com.example.erabook.util.loadImageFromUrl


class FavoriteAdapter(private val itemClickListener: (Int) -> Unit) :
    ListAdapter<VolumeInfo, FavoriteAdapter.ViewHolder>(com.example.erabook.adapters.FavoriteAdapterDiffCallBack()) {


    inner class ViewHolder(private val binding: ItemFavoriteBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(books: VolumeInfo) {
            binding.apply {
                bookTitle.text = books.title
                bookAuthor.text = books.authors[0]
                favoriteBookItem.setOnClickListener {
                    removeItem(adapterPosition)
                }
                itemView.setOnClickListener {
                    val bookItem = bundleOf(Pair("favoriteBook", books))
                    itemView.findNavController().navigate(R.id.favoriteToDetails, bookItem)
                }
                bookImage.loadImageFromUrl(books.imageLinks?.thumbnail)
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
            itemClickListener(position)
        })
    }
}

class FavoriteAdapterDiffCallBack : DiffUtil.ItemCallback<VolumeInfo>() {
    override fun areItemsTheSame(oldItem: VolumeInfo, newItem: VolumeInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: VolumeInfo, newItem: VolumeInfo): Boolean {
        return oldItem == newItem
    }
}
