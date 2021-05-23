package com.example.pruebaalvic.api

import com.example.pruebaalvic.model.Photo
import retrofit2.http.GET

interface Service {

    @GET("photos")
    suspend fun getPhotos(): List<Photo>
}