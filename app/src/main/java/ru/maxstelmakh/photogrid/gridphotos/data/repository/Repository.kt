package ru.maxstelmakh.photogrid.gridphotos.data.repository

import kotlinx.coroutines.flow.Flow
import ru.maxstelmakh.photogrid.gridphotos.data.model.SearchedPhotos
import ru.maxstelmakh.photogrid.utils.Result

interface Repository {
    suspend fun fetch(): Flow<Result<SearchedPhotos>>
}