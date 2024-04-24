package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.R
import com.example.erabook.data.models.nyt.Books
import com.example.erabook.databinding.ItemNytBinding

class NYTChildAdapter :
    ListAdapter<Books, NYTChildAdapter.NYTChildViewHolder>(NYTChildAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NYTChildViewHolder {
        return NYTChildViewHolder(
            ItemNytBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NYTChildViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NYTChildViewHolder(private val binding: ItemNytBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Books) {
            binding.apply {
                book = item
                itemView.setOnClickListener {
                    val bookItem = bundleOf(Pair("nytBookTitle", item.title))
                    itemView.findNavController().navigate(R.id.homeToDetails, bookItem)
                }
            }
        }
    }
}

class NYTChildAdapterDiffCallBack : DiffUtil.ItemCallback<Books>() {
    override fun areItemsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }
}
