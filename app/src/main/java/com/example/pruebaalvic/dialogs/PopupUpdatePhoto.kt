package com.example.pruebaalvic.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.pruebaalvic.R
import com.example.pruebaalvic.model.Photo

class PopupUpdatePhoto(private val activity: Activity, private val photo: Photo,
                       private val clickListenerPopup: ClickListenerPopup) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
            val v: View = inflater.inflate(R.layout.dialog_update, null)
            builder.setView(v)

            val textidPhoto = v.findViewById<TextView>(R.id.textidPhoto)
            val btnUpdate = v.findViewById<Button>(R.id.btnUpdate)
            val btnCancel = v.findViewById<Button>(R.id.btnCancel)
            val edtTitle = v.findViewById<EditText>(R.id.edtTitle)
            val edtUrlImage = v.findViewById<EditText>(R.id.edtUrlImage)

            textidPhoto.text = photo.id.toString()
            edtTitle.setText(photo.title)
            edtUrlImage.setText(photo.url)

            btnUpdate.setOnClickListener {
                dismiss()
                val photoUpdated = Photo(
                        id = photo.id,
                        title = edtTitle.text.toString(),
                        url = edtUrlImage.text.toString(),
                        albumId = photo.albumId,
                        thumbnailUrl = photo.thumbnailUrl
                )
                clickListenerPopup.onItemClick(photoUpdated, 0)
            }

            btnCancel.setOnClickListener { dismiss()}

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}
