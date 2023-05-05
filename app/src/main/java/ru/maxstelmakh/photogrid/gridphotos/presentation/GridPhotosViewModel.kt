package ru.maxstelmakh.photogrid.gridphotos.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.maxstelmakh.photogrid.gridphotos.domain.usecase.GetPhotos
import ru.maxstelmakh.photogrid.utils.DefaultDispatcherProvider
import javax.inject.Inject

@HiltViewModel
class GridPhotosViewModel @Inject constructor(
    getPhotosUC: GetPhotos,
    private val dispatcherProvider: DefaultDispatcherProvider
) : ViewModel() {

    init {
        getPhotos()
    }

    private fun getPhotos() {

    }
}