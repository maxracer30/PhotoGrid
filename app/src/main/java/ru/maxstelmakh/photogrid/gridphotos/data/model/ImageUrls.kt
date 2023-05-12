package ru.maxstelmakh.photogrid.gridphotos.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUrls(
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
) : Parcelable