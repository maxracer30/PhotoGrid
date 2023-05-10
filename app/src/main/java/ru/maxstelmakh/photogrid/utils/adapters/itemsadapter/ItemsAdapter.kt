package ru.maxstelmakh.photogrid.utils.adapters.itemsadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.photogrid.databinding.ItemImageCardBinding
import ru.maxstelmakh.photogrid.databinding.ItemImageShimmerBinding
import ru.maxstelmakh.photogrid.gridphotos.presentation.viewholders.ImageShimmerVHolder
import ru.maxstelmakh.photogrid.gridphotos.presentation.viewholders.ImageVHolder

class ItemsAdapter(
    private val onClick: ((Click) -> Unit)? = null,
) : PagingDataAdapter<ItemsAdapterModel, RecyclerView.ViewHolder>(ItemsDiffCallback()) {

    override fun getItemViewType(position: Int) =
        checkNotNull(getItem(position)).getViewType().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ItemViewType.values()[viewType]) {
            ItemViewType.IMAGE -> {
                val itemBinding =
                    ItemImageCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ImageVHolder(view = itemBinding, onClick = onClick)
            }

            ItemViewType.IMAGE_SHIMMER -> {
                val itemBinding =
                    ItemImageShimmerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ImageShimmerVHolder(view = itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it.getViewType()) {
                ItemViewType.IMAGE -> (holder as ImageVHolder).bind(item = it)
                ItemViewType.IMAGE_SHIMMER -> (holder as ImageShimmerVHolder).bind()
            }
        }
    }
}