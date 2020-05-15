package com.example.simpleimageviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = imagePath.substring(imagePath.lastIndexOf("/") + 1)
        binding.mainImage.setImage(ImageSource.uri(imagePath))
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
