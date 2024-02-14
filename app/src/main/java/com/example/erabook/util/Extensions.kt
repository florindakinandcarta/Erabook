package com.example.erabook.util

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageLink: String?) {
    imageLink?.let {
        val absolutePath = "file:///android_asset/$imageLink"
        Glide.with(this)
            .load(absolutePath)
            .into(this)
    }
}

fun Context.showToast(messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageResId, duration).show()
}