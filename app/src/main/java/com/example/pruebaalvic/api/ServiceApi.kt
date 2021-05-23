package com.example.pruebaalvic.api

import com.example.pruebaalvic.model.Photo
import com.example.pruebaalvic.model.errorcontroller.Resource
import com.example.pruebaalvic.model.errorcontroller.ResponseHandler
import javax.inject.Inject

class ServiceApi @Inject constructor(private val service: Service) : ResponseHandler() {

    suspend fun loadPhotos() : Resource<List<Photo>> {
        return try {
            val response = service.getPhotos()
            return handleSuccess(response)
        }catch (e: Exception){
            handleException(e)
        }
    }
}