package ru.maxstelmakh.photogrid.gridphotos.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.photogrid.databinding.ItemImageCardBinding
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.Click
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel
import ru.maxstelmakh.photogrid.utils.slowAnimLoad

class ImageVHolder(
    private val view: ItemImageCardBinding,
    private val onClick: ((Click) -> Unit)? = null,
) : RecyclerView.ViewHolder(view.root) {

    fun bind(item: ItemsAdapterModel) = with(view) {
        val model = item.getItem() as Image
        root.transitionName = model.id
        imageView.apply {
            slowAnimLoad(model.urls.thumb, model.urls.small)
            setOnClickListener {
                onClick?.invoke(Click(view = view, item = model))
            }
        }
    }
}