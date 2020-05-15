package com.example.simpleimageviewer

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource

class ImageViewHolder(val imageView: SubsamplingScaleImageView) : RecyclerView.ViewHolder(imageView)

class ImageGridAdapter(data: Array<String> = arrayOf()) :
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
        val view = layoutInflater.inflate(R.layout.box_image, parent, false)
                as SubsamplingScaleImageView
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imageList[position]
        holder.imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
        holder.imageView.setImage(ImageSource.uri(imagePath))
        holder.imageView.setOnClickListener {
            it.context.startActivity(Intent(it.context, ImageActivity::class.java).apply {
                putExtra("imagePath", imagePath)
            })
        }
    }
}