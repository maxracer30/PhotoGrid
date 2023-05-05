package ru.maxstelmakh.photogrid.gridphotos.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.maxstelmakh.photogrid.gridphotos.data.model.SearchedPhotos
import ru.maxstelmakh.photogrid.gridphotos.data.repository.Repository
import ru.maxstelmakh.photogrid.utils.Result
import javax.inject.Inject

class GetPhotos @Inject constructor(
    private val repository: Repository
) {

    private val _state = MutableStateFlow(SearchedPhotos(emptyList()))
    val state = _state.asStateFlow()

    suspend fun fetch() {
        repository.fetch().collect { result ->
            when (result) {
                is Result.Success -> {
                    _state.emit(result.data)
                }

                is Result.Failure -> {
                    if (_state.value.results.isNotEmpty()) _state.emit(_state.value)
                }
            }
        }
    }
}
