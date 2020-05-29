package com.example.simpleimageviewer

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image_display)
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
        holder.image.setImageBitmap(decodeSampledBitmap(holder.image, imagePath))
//        holder.image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
//        holder.image.setImage(ImageSource.uri(imagePath).scaleDown())
        holder.image.transitionName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
        holder.image.setOnClickListener {
            clickListener.onAnimalItemClick(position, imagePath, holder.image)
        }
        holder.text.text = imagePath.substring(imagePath.lastIndexOf("/") + 1)
    }

    private fun decodeSampledBitmap(image: ImageView, path: String): Bitmap? {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)
            image.tag = intArrayOf(this.outWidth, this.outHeight)
//            Log.i("SAMPLE SIZE", this.outWidth.toString() + " " + this.outHeight.toString())
//            Log.i("SAMPLE SIZE", image.measuredWidth.toString() + " " + image.measuredHeight.toString())
            inSampleSize = calculateInSampleSize(this, 200, 130)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(path, this)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}

interface AnimalItemClickListener {
    fun onAnimalItemClick(pos: Int, imagePath : String, shareImageView : ImageView)
}
