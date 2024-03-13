package com.example.erabook.util

import android.content.Context
import android.support.annotation.StringRes
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImageFromAssets")
fun ImageView.loadImageFromAssets(imageLink: String?) {
    imageLink?.let {
        val absolutePath = "file:///android_asset/$imageLink"
        Glide.with(this)
            .load(absolutePath)
            .into(this)
    }
}
@BindingAdapter("loadImage")
fun ImageView.loadImageFromUrl(imageLink: String?) {
    var imageLinkModified = ""
    if (imageLink?.startsWith("http://") == true){
        imageLinkModified = "https://" + imageLink.substring(7)
    }
    imageLink?.let {
        Glide.with(this)
            .load(imageLinkModified)
            .into(this)
    }
}

fun Context.showToast(@StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageResId, duration).show()
}