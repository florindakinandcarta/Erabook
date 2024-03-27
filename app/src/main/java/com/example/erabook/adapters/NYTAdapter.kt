package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.data.models.nyt.Books
import com.example.erabook.databinding.ItemNytBinding

class NYTAdapter : ListAdapter<Books, NYTAdapter.ViewHolder>(NYTAdapterDiffCallBack()) {
    inner class ViewHolder(private val binding: ItemNytBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Books) {
            binding.apply {
                book = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNytBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NYTAdapterDiffCallBack : DiffUtil.ItemCallback<Books>() {
    override fun areItemsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }
}