package com.example.pruebaalvic.dialogs

import com.example.pruebaalvic.model.Photo

interface ClickListenerPopup {
    fun onItemClick(photo: Photo, idClick: Int)
}