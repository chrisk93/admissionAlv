package com.example.pruebaalvic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pruebaalvic.R
import com.example.pruebaalvic.model.Photo
import kotlinx.android.synthetic.main.item_photo.view.*
import javax.inject.Inject

class AdapterPhotos @Inject constructor(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemsPhoto: List<Photo> = emptyList()

    fun setData(itemsPhoto: List<Photo>) {
        this.itemsPhoto = itemsPhoto
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemsPhoto.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(itemsPhoto[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photo: Photo, listener: OnItemClickListener) = with(itemView) {
            namePhoto.text = photo.title
            Glide.with(itemView)
                    .load(photo.url)
                    .placeholder(R.drawable.image_not)
                    .into(imgPhoto)

            edit.setOnClickListener {
                listener.onItemClick(photo, it, 0)
            }

            delete.setOnClickListener {
                listener.onItemClick(photo, it, 1)
            }
        }

    }
}