package com.example.pruebaalvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photo")
data class Photo (
    @SerializedName("albumId") val albumId: Int,
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("url") var url : String,
    @SerializedName("thumbnailUrl") val thumbnailUrl : String
)