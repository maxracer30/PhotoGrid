package ru.maxstelmakh.photogrid.gridphotos.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel

interface PhotoRepository {
    suspend fun fetch(search: String): Flow<PagingData<ItemsAdapterModel>>

}