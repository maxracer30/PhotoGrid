package ru.maxstelmakh.photogrid.gridphotos.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemViewType
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel

@Parcelize
data class Image(
    val id: String,
    val urls: ImageUrls,
): ItemsAdapterModel, Parcelable {
    override fun getItemId() = id

    override fun getViewType() = ItemViewType.IMAGE

    override fun getItem(): Any = this
}