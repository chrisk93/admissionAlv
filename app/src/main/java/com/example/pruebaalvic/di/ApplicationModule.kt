package com.example.pruebaalvic.di

import android.content.Context
import androidx.room.Room
import com.example.pruebaalvic.BuildConfig
import com.example.pruebaalvic.api.Service
import com.example.pruebaalvic.db.DaoDB
import com.example.pruebaalvic.db.PhotosDB
import com.example.pruebaalvic.db.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PhotosDB {
        return Room.databaseBuilder(
            appContext,
            PhotosDB::class.java,
            "photosDB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDaoDB(photosDB: PhotosDB): DaoDB = photosDB.daoDB()

    @Provides
    @Singleton
    fun providePhotosRepository(dao : DaoDB): PhotosRepository = PhotosRepository(dao)

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): Service = retrofit.create(Service::class.java)
}