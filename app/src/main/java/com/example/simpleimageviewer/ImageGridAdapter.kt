package com.example.simpleimageviewer

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

class ImageGridAdapter(data: List<String> = listOf()) :
    RecyclerView.Adapter<ImageViewHolder>() {
    var imageList = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = imageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // create new ImageView
        val view = layoutInflater.inflate(R.layout.box_image, parent, false) as ImageView
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imageList[position]
        holder.imageView.setImageURI(Uri.parse(imagePath))
        holder.imageView.setOnClickListener {
            it.context.startActivity(Intent(it.context, ImageActivity::class.java).apply {
                putExtra("imagePath", imagePath)
            })
        }
    }
}