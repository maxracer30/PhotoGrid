package ru.maxstelmakh.photogrid.utils.adapters.itemsadapter

import ru.maxstelmakh.photogrid.databinding.ItemImageCardBinding

data class Click(
    val view: ItemImageCardBinding,
    val item: ItemsAdapterModel,
)