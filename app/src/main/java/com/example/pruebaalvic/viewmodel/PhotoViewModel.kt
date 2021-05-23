package com.example.pruebaalvic.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pruebaalvic.api.ServiceApi
import com.example.pruebaalvic.db.PhotosRepository
import com.example.pruebaalvic.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val service: ServiceApi,
    private val usuariosRepository: PhotosRepository) : ViewModel() {

    suspend fun getPhotos() {
        val data = service.loadPhotos().data ?: emptyList()
        insertAllPhotos(data)
    }

    suspend fun insertPhoto(photo: Photo) = usuariosRepository.insertPhoto(photo)

    suspend fun updatePhoto(photo: Photo) = usuariosRepository.updatePhoto(photo)

    suspend fun deletePhoto(photo: Photo) = usuariosRepository.deletePhoto(photo)

    suspend fun getAllPhotos() = usuariosRepository.getPhotos()

    private suspend fun insertAllPhotos(photos: List<Photo>) = usuariosRepository.insertAllPhotos(photos)

}