package com.example.pruebaalvic

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.pruebaalvic.db.DaoDB
import com.example.pruebaalvic.db.PhotosDB
import com.example.pruebaalvic.model.Photo
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.jvm.Throws

@RunWith(JUnit4::class)
class TestDb {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dbDao: DaoDB
    private lateinit var db: PhotosDB

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, PhotosDB::class.java).build()
        dbDao = db.daoDB()
    }

    @After
    fun close(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun saveUsuario() = runBlocking {
        val photo =  Photo(23, 122,"test", "www.gleoe.com", "www.gleoe.com")
        dbDao.insertPhoto(photo = photo)

        val allphotos = dbDao.getAllPhotos()
        Truth.assertThat(allphotos.value?.get(0)).isEqualTo(photo)
    }
}