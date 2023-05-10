package ru.maxstelmakh.photogrid.gridphotos.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel

typealias PhotoPageLoader = suspend (page: Int, perPage: Int) -> List<Image>

class PhotoPagingSource(
    private val loader: PhotoPageLoader,
) : PagingSource<Int, ItemsAdapterModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsAdapterModel> {
        val currentPage = params.key ?: 1

        return try {
            val photos = loader.invoke(currentPage, params.loadSize)

            LoadResult.Page(
                data = photos,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, ItemsAdapterModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null

        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}