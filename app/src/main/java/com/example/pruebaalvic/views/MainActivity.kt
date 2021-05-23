package com.example.pruebaalvic.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pruebaalvic.BuildConfig
import com.example.pruebaalvic.R
import com.example.pruebaalvic.adapters.AdapterPhotos
import com.example.pruebaalvic.adapters.OnItemClickListener
import com.example.pruebaalvic.dialogs.ClickListenerPopup
import com.example.pruebaalvic.dialogs.PopupUpdatePhoto
import com.example.pruebaalvic.model.Photo
import com.example.pruebaalvic.utils.Permissions
import com.example.pruebaalvic.utils.Utils
import com.example.pruebaalvic.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: PhotoViewModel by viewModels()
    private val adapter = AdapterPhotos(this)

    private lateinit var photoFile: File
    private val PICKFILE_RESULT_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val pattern = "yyyyMMdd_HHmmss"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkStatusPermissions()

        recyclerPhotos.layoutManager = GridLayoutManager(this, 2)
        recyclerPhotos.adapter = adapter

        loadData()
        showData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (Utils.isInternetAvailable(this)) {
            when (item.itemId) {
                R.id.optCamera -> {
                    openCamera()

                    return true
                }
                R.id.optGallery -> {
                    openGallery()

                    return true
                }
            }
        } else {
            showMessage(getString(R.string.no_conection))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPhotos()
        }
    }

    private fun showData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllPhotos()
                    .observe(this@MainActivity, Observer {
                        it?.let {
                            println(it.toString())
                            adapter.setData(it)
                        }
                    })
        }
    }

    override fun onItemClick(photo: Photo, itemView: View, idClick: Int) {
        if (Utils.isInternetAvailable(this)) {
            optionsPhotoSelected(idClick, photo)
        } else {
            showMessage(getString(R.string.no_conection))
        }
    }

    private fun optionsPhotoSelected(idClick: Int, photo: Photo) {
        when (idClick) {
            0 -> {
                updatePhoto(photo)
            }
            1 -> {
                deletePhoto(photo)
            }
        }
    }

    private fun deletePhoto(photo: Photo) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.deletePhoto(photo)
            showMessage(getString(R.string.delete_success))
        }
    }

    private fun updatePhoto(photo: Photo) {
        val popupUpdate = PopupUpdatePhoto(this, photo, object : ClickListenerPopup {
            override fun onItemClick(photo: Photo, idClick: Int) {
                when (idClick) {
                    0 -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.updatePhoto(photo)
                            showMessage(getString(R.string.update_success))
                        }
                    }
                }
            }
        })
        popupUpdate.show(supportFragmentManager, "update_photo")
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.REQUEST_PERMISSIONS_CAMERA && grantResults.isNotEmpty() &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun checkStatusPermissions() {
        val statusCamera = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        )
        val statusFiles = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (statusCamera != PackageManager.PERMISSION_GRANTED ||
                statusFiles != PackageManager.PERMISSION_GRANTED
        ) {
            setupPermissions()
        }
    }

    private fun setupPermissions() {
        Permissions.requestPermissionCamera(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICKFILE_RESULT_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null
        ) {
            //TODO pending implement
        } else if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            insertPhoto()
        }

    }

    private fun openGallery() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_document)),
                PICKFILE_RESULT_CODE
        )
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            photoFile = createImageFile()
            val fileProvider = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.plus(".fileprovider"), photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            takePictureIntent.resolveActivity(this.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(pattern).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )

        return image
    }

    private fun insertPhoto() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.insertPhoto(
                    Photo(id = Random().nextInt(),
                            albumId = 2,
                            title = photoFile.name,
                            thumbnailUrl = photoFile.path,
                            url = photoFile.path
                    )
            )
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}