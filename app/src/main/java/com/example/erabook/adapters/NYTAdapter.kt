package com.example.erabook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.data.models.nyt.Lists
import com.example.erabook.databinding.ItemNytRecycleViewBinding
import com.example.erabook.fragments.home.tabFragments.nyt.NYTItemClickListener

class NYTAdapter(private val clickListener: NYTItemClickListener? = null) : ListAdapter<Lists, NYTAdapter.NYTViewHolder>(NYTAdapterDiffCallBack()) {
    private lateinit var nytChildAdapter: NYTChildAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NYTViewHolder {
        return NYTViewHolder(
            ItemNytRecycleViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NYTViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class NYTViewHolder(private val binding: ItemNytRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            nytChildAdapter = NYTChildAdapter()
            binding.childList.apply {
                adapter = nytChildAdapter
            }
        }
        fun bind(item: Lists) {
            binding.apply {
                childTitle.text = item.listName
                childTitle.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        clickListener?.onNYTItemClick(getItem(adapterPosition).listName)
                    }
                }
                nytChildAdapter.submitList(item.books)
            }
        }
    }
}

class NYTAdapterDiffCallBack : DiffUtil.ItemCallback<Lists>() {
    override fun areItemsTheSame(oldItem: Lists, newItem: Lists): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Lists, newItem: Lists): Boolean {
        return oldItem == newItem
    }
}