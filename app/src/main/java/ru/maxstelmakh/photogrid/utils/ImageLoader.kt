package ru.maxstelmakh.photogrid.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.maxstelmakh.photogrid.R

private const val ANIMATION_DURATION = 500

fun ImageView.slowAnimLoad(thumbUrl: String, smallUrl: String) {
    val thumbnail = Glide.with(context)
        .load(thumbUrl)
    Glide.with(context)
        .load(smallUrl)
        .thumbnail(thumbnail)
        .placeholder(R.drawable.placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(ANIMATION_DURATION))
        .error(R.drawable.baseline_error_outline_24)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

fun ImageView.fastAnimLoad(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.baseline_error_outline_24)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}