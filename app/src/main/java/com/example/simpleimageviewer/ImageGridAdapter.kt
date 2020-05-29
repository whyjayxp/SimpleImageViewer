package com.example.simpleimageviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: SubsamplingScaleImageView = itemView.findViewById(R.id.image_display)
    val text: TextView = itemView.findViewById(R.id.image_text)
}

class ImageGridAdapter(private val viewModel: ImageGridViewModel,
                       private val clickListener: AnimalItemClickListener) :
    RecyclerView.Adapter<ImageViewHolder>() {

    override fun getItemCount() = viewModel.imageList.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.box_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = viewModel.imageList.value?.get(position) ?: ""
        holder.image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
        holder.image.setImage(ImageSource.uri(imagePath))
        holder.image.transitionName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
        holder.image.setOnClickListener {
            clickListener.onAnimalItemClick(position, imagePath, holder.image)
        }
        holder.text.text = imagePath.substring(imagePath.lastIndexOf("/") + 1)
    }
}

interface AnimalItemClickListener {
    fun onAnimalItemClick(pos: Int, imagePath : String, shareImageView : SubsamplingScaleImageView)
}
