package ru.maxstelmakh.photogrid.utils.adapters.itemsadapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ItemsDiffCallback : DiffUtil.ItemCallback<ItemsAdapterModel>() {

    override fun areItemsTheSame(oldItem: ItemsAdapterModel, newItem: ItemsAdapterModel): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ItemsAdapterModel,
        newItem: ItemsAdapterModel
    ): Boolean {
        return oldItem.getItem() == newItem.getItem()
    }
}
