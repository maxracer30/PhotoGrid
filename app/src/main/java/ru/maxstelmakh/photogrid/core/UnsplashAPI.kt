package ru.maxstelmakh.photogrid.core

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.maxstelmakh.photogrid.BuildConfig.TEST_CLIENT_ID
import ru.maxstelmakh.photogrid.BuildConfig.TEST_CLIENT_ID1
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.gridphotos.data.model.SearchedPhotos

interface UnsplashAPI {

    @Headers("Authorization: Client-ID $TEST_CLIENT_ID1")
    @GET("photos")
    suspend fun fetchPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Image>>

    @Headers("Authorization: Client-ID $TEST_CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") searchPhotosName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<SearchedPhotos>
}