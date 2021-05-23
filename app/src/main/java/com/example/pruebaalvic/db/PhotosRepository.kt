package com.example.pruebaalvic.db

import androidx.lifecycle.LiveData
import com.example.pruebaalvic.model.Photo
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val dao : DaoDB) {

    suspend fun getPhotos(): LiveData<List<Photo>> = dao.getAllPhotos()

    suspend fun insertPhoto(photo: Photo): Long = dao.insertPhoto(photo)

    suspend fun updatePhoto(photo: Photo) : Int = dao.updatePhoto(photo)

    suspend fun deletePhoto(photo: Photo) : Int = dao.deletePhoto(photo)

    suspend fun insertAllPhotos(photos: List<Photo>) = dao.insertAllPhotos(photos)

}