package com.example.pruebaalvic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pruebaalvic.model.Photo

@Database(entities = [Photo::class],
    version = 1,
    exportSchema = false
)
abstract class PhotosDB : RoomDatabase() {
    abstract fun daoDB() : DaoDB
}