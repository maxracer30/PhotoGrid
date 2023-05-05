package ru.maxstelmakh.photogrid.core

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.maxstelmakh.photogrid.utils.DefaultDispatcherProvider
import ru.maxstelmakh.photogrid.utils.DispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface BindAppModule {

    @Binds
    @Singleton
    fun dispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

}