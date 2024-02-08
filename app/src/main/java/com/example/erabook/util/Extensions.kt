package com.example.erabook.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("test")
fun ImageView.loadImage(imageLink: String?) {
    imageLink?.let {
        val absolutePath = "file:///android_asset/$imageLink"
        Glide.with(this)
            .load(absolutePath)
            .into(this)
    }
}