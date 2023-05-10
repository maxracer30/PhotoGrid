package ru.maxstelmakh.photogrid.gridphotos.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUrls(
    val regular: String,
    val thumb: String,
) : Parcelable