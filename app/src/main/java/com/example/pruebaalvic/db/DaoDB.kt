package com.example.pruebaalvic.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pruebaalvic.model.Photo

@Dao
interface DaoDB {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Update
    suspend fun updatePhoto(photo: Photo) : Int

    @Delete
    suspend fun deletePhoto(photo: Photo) : Int

}