package ru.maxstelmakh.photogrid.core

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.maxstelmakh.photogrid.utils.DefaultDispatcherProvider
import ru.maxstelmakh.photogrid.utils.DispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
interface BindAppModule {

    @Binds
    fun dispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

}