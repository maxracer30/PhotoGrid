package ru.maxstelmakh.photogrid.utils.adapters.itemsadapter

interface ItemsAdapterModel {

    fun getItemId(): String

    fun getViewType(): ItemViewType

    fun getItem(): Any
}