package ru.maxstelmakh.photogrid.gridphotos.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.maxstelmakh.photogrid.gridphotos.data.repository.DefaultPhotoRepository
import ru.maxstelmakh.photogrid.gridphotos.data.repository.PhotoRepository

@Module
@InstallIn(ViewModelComponent::class)
interface PhotoModule {

    @Binds
    fun photoRepository(repository: DefaultPhotoRepository): PhotoRepository

}