package com.example.simpleimageviewer

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import android.view.MenuItem
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.simpleimageviewer.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        with (window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            val transition: Transition = TransitionInflater.from(context).inflateTransition(R.transition.thumb_transition)
            sharedElementEnterTransition = transition
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image)
        postponeEnterTransition()

        val imagePath = intent.getStringExtra("imagePath") ?: ""
        val byteArray = intent.getByteArrayExtra("bitmapArray")
        val bitmap = if (byteArray == null) null else BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val width = intent.getIntArrayExtra("dimens")[0]
        val height = intent.getIntArrayExtra("dimens")[1]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = imagePath.substring(imagePath.lastIndexOf("/") + 1)

        val state = savedInstanceState?.getSerializable("imageViewState") as ImageViewState?
        binding.mainImage.transitionName = intent.getStringExtra("transitionName")
        binding.mainImage.setMinimumDpi(50)
        binding.mainImage.setImage(ImageSource.uri(imagePath).dimensions(width, height),
            if (bitmap == null) null else ImageSource.bitmap(bitmap), state)
        binding.mainImage.setOnImageEventListener(object :
            SubsamplingScaleImageView.DefaultOnImageEventListener() {
            override fun onReady() {
                startPostponedEnterTransition()
                binding.mainImage.setOnImageEventListener(null)
            }
        })
        binding.mainImage.setOnClickListener {
            finishAfterTransition()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (binding.mainImage.state != null) {
            outState.putSerializable("imageViewState", binding.mainImage.state)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
