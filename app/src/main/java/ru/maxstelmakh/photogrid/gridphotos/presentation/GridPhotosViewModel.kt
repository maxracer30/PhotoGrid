package ru.maxstelmakh.photogrid.gridphotos.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import ru.maxstelmakh.photogrid.gridphotos.data.repository.PhotoRepository
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapterModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class GridPhotosViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    var items: Flow<PagingData<ItemsAdapterModel>>

    val searchBy = MutableLiveData("")

    init {
        items =
            searchBy
                .asFlow()
                .debounce(1000)
                .flatMapLatest {
                    photoRepository.fetch()
                }
                .cachedIn(viewModelScope)
    }

    fun refresh() {
        //todo
    }
}