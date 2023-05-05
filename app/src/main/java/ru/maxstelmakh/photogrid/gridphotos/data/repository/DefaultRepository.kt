package ru.maxstelmakh.photogrid.gridphotos.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.maxstelmakh.photogrid.core.UnsplashAPI
import ru.maxstelmakh.photogrid.core.apiCall
import ru.maxstelmakh.photogrid.gridphotos.data.model.SearchedPhotos
import ru.maxstelmakh.photogrid.utils.DefaultDispatcherProvider
import ru.maxstelmakh.photogrid.utils.Result
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: UnsplashAPI,
    private val dispatcherProvider: DefaultDispatcherProvider,
) : Repository {
    override suspend fun fetch(): Flow<Result<SearchedPhotos>> = flow {
        apiCall(dispatcherProvider) { api.fetchPhotos(1) }
    }
}