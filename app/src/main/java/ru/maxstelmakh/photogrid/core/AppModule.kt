package ru.maxstelmakh.photogrid.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.maxstelmakh.photogrid.BuildConfig
import ru.maxstelmakh.photogrid.utils.DefaultDispatcherProvider

@Module(includes = [BindAppModule::class])
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    fun interceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun okHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor = interceptor)
            .build()
    }

    @Provides
    fun provideAPIService(okHttpClient: OkHttpClient): UnsplashAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashAPI::class.java)
    }

    @Provides
    fun provideDefaultDispatcherProvider(): DefaultDispatcherProvider {
        return DefaultDispatcherProvider()
    }

}