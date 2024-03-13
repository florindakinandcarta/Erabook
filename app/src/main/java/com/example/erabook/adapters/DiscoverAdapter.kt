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
import com.example.erabook.databinding.ItemGoogleSearchBinding

class DiscoverAdapter :
    ListAdapter<VolumeInfo, DiscoverAdapter.ViewHolder>(DiscoverAdapterDiffCallBack()) {
    private val cardColors = listOf(
        R.drawable.card_background_1,
        R.drawable.card_background_2,
        R.drawable.card_background_3,
        R.drawable.card_background_4
    )

    inner class ViewHolder(private val binding: ItemGoogleSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: VolumeInfo) {
            binding.apply {
                book = items
                cardBackground.setBackgroundResource(cardColors[adapterPosition % cardColors.size])
                itemView.setOnClickListener {
                    val bookItem = bundleOf(Pair("bookName", items.title))
                    itemView.findNavController().navigate(R.id.discoverToDetails, bookItem)
                }
                this.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGoogleSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiscoverAdapterDiffCallBack : DiffUtil.ItemCallback<VolumeInfo>() {
    override fun areItemsTheSame(oldItem: VolumeInfo, newItem: VolumeInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: VolumeInfo, newItem: VolumeInfo): Boolean {
        return oldItem == newItem
    }
}