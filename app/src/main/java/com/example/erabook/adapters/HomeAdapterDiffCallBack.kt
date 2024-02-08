package com.example.erabook.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.erabook.data.Books

class HomeAdapterDiffCallBack: DiffUtil.ItemCallback<Books>() {
    override fun areItemsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Books, newItem: Books): Boolean {
        return oldItem == newItem
    }
}