package com.example.simpleimageviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.simpleimageviewer.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image)

        val imagePath = intent.getStringExtra("imagePath") ?: ""

        title = imagePath.substring(imagePath.lastIndexOf("/") + 1)
        binding.mainImage.setImage(ImageSource.uri(imagePath))
    }
}
