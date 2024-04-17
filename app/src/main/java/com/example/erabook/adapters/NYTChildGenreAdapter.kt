package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.data.models.nyt.Books
import com.example.erabook.databinding.ItemNytGenreBinding

class NYTChildGenreAdapter :
    ListAdapter<Books, NYTChildGenreAdapter.NYTChildViewHolder>(NYTChildAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NYTChildViewHolder {
        return NYTChildViewHolder(
            ItemNytGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NYTChildViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NYTChildViewHolder(private val binding: ItemNytGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Books) {
            binding.apply {
                book = item
            }
        }
    }
}

