package ru.maxstelmakh.photogrid.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.maxstelmakh.photogrid.R

private const val ANIMATION_DURATION = 500

fun ImageView.imageFromUrl(url: String) {

    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(ANIMATION_DURATION))
        .transform(RoundedCorners(20))
//        .error() todo
//        .fallback() todo
        .into(this)
}