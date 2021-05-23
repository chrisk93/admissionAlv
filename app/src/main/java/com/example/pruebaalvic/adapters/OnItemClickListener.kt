package com.example.pruebaalvic.adapters

import android.view.View
import com.example.pruebaalvic.model.Photo

interface OnItemClickListener {
    fun onItemClick(photo: Photo, itemView: View, idClick: Int)
}