package com.example.erabook.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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
    if (imageLink?.startsWith("http://") == true) {
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
fun openLinkBrowser(url: String?, context: Context) {
    val intent =
        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.barnesandnoble.com/s/$url"))
    startActivity(context, intent, null)
}