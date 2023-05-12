package ru.maxstelmakh.photogrid.gridphotos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.maxstelmakh.photogrid.core.UnsplashAPI
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.gridphotos.data.paging.PhotoPageLoader
import ru.maxstelmakh.photogrid.gridphotos.data.paging.PhotoPagingSource
import ru.maxstelmakh.photogrid.utils.DispatcherProvider
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val api: UnsplashAPI,
    private val dispatcherProvider: DispatcherProvider
) : PhotoRepository {

    override suspend fun fetch(search: String): Flow<PagingData<ItemsAdapterModel>> {
        val loader: PhotoPageLoader = { page, perPage ->
            getPhotos(page, perPage, search)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = PREFETCH_DISTANCE,
                maxSize = MAX_SIZE,
            ),
            pagingSourceFactory = {
                PhotoPagingSource(
                    loader = loader,
                )
            },
        ).flow
    }

    private suspend fun getPhotos(
        page: Int,
        perPage: Int,
        search: String
    ): List<Image> = withContext(dispatcherProvider.io) {
        return@withContext if (search.isBlank()) {
            val result = api.fetchPhotos(page = page, perPage = perPage)
            when (result.isSuccessful) {
                true -> {
                    result.body() ?: emptyList()
                }

                false -> {
                    emptyList<Image>()
                }
            }
        } else {
            val result = api.searchPhotos(
                searchPhotosName = search,
                page = page,
                perPage = perPage
            )
            when (result.isSuccessful) {
                true -> {
                    result.body()?.results ?: emptyList()
                }

                false -> {
                    emptyList<Image>()
                }
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 30
        const val PREFETCH_DISTANCE = 7
        const val MAX_SIZE = 220
    }
}
