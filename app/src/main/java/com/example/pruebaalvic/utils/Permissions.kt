package com.example.pruebaalvic.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat

object Permissions {

    private const val permissionCamera = Manifest.permission.CAMERA
    private const val permissionWriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private const val permissionReadExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE
    const val REQUEST_PERMISSIONS_CAMERA = 1

    fun requestPermissionCamera(context: Context) {
        ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                        permissionCamera,
                        permissionReadExternalStorage,
                        permissionWriteExternalStorage
                ), REQUEST_PERMISSIONS_CAMERA
        )
    }
}