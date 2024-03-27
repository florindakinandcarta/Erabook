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
import com.example.erabook.R

@BindingAdapter("loadImageFromUrlNYT")
fun ImageView.loadImage(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
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
fun openLinkBookDownload(url: String?, context: Context) {
    val intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}

fun Context.createBookDetailsIntent(
    title: String?,
    author: String?,
    pageCount: String?,
    publishedDate: String?
): Intent {
    val intent = Intent(Intent.ACTION_SEND)
        .apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT, getString(
                    R.string.share_book,
                    title,
                    author
                        ?: "Authors not found",
                    pageCount,
                    publishedDate
                )
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.share_book_subject)
            )
        }
    return intent
}
fun Context.startBookDetailsIntent(
    title: String?,
    author: String?,
    pageCount: String?,
    publishedDate: String?
) {
    val bookDetailsIntent = createBookDetailsIntent(title, author, pageCount, publishedDate)
    val chooserIntent = Intent.createChooser(
        bookDetailsIntent,
        getString(R.string.send_details)
    )
    startActivity(chooserIntent)
}