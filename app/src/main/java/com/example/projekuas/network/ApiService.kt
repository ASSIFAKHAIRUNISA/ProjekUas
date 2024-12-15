package com.example.projekuas.network

import com.example.projekuas.model.Gallery
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("oTtWf/gallery")
    fun getGallery(): Call<List<Gallery>>

    @POST("oTtWf/gallery")
    fun createGallery(@Body model: Gallery): Call<Gallery>

    @DELETE("oTtWf/gallery/{id}")
    fun deleteGallery(@Path("id") id: String): Call<Void>

    @GET("oTtWf/gallery/{id}")
    fun getGalleryById(@Path("id") id: String): Call<Gallery>

    @POST("oTtWf/gallery/{id}")
    fun updateGallery(
        @Path("id") id: String,  // ID harus valid saat update
        @Body gallery: Gallery
    ): Call<Gallery>
}