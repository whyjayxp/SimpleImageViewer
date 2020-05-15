package com.example.simpleimageviewer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.simpleimageviewer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: ImageGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.searchButton.setOnClickListener { processInput(it) }

        myAdapter = ImageGridAdapter(savedInstanceState?.getStringArray("imageList") ?: arrayOf())
        binding.imageGrid.apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(this@MainActivity, 3)
            } else {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
            }
            adapter = myAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray("imageList", myAdapter.imageList)
    }

    private fun processInput(view : View) {
        // hide input keyboard
        val iMM = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMM.hideSoftInputFromWindow(view.windowToken, 0)

        // check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            searchPath()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode != 1) return
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            searchPath()
        } else {
            Toast.makeText(this, R.string.no_permission_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isSupportedType(file: File) = when (file.extension.toLowerCase()) {
        "png", "jpeg", "jpg" -> true
        else -> false
    }

    private fun searchPath() {
        val inputPath = binding.pathInput.text.toString()
        binding.message.text = ""
        myAdapter.imageList = arrayOf()

        val file = File(inputPath)
        if (inputPath == "" || !file.exists()) { // will there be SecurityException?
            binding.message.text = getString(R.string.illegal_path_error)
            return
        }

        if (file.isDirectory) {
            val files = file.listFiles { f : File -> isSupportedType(f) }
            if (files == null || files.isEmpty()) { // why would files be null? (eg. storage/self)
                binding.message.text = getString(R.string.no_supported_files_error)
            } else {
                myAdapter.imageList = files.map { it.toString() }.toTypedArray()
            }
        } else {
            if (isSupportedType(file)) {
                // show image
                  myAdapter.imageList = arrayOf(inputPath)
            } else {
                // show filename
                binding.message.text = file.name
            }
        }
    }

    private fun showImage(view : View) {
        val intent = Intent(this, ImageActivity::class.java).apply {
            putExtra("imagePath", view.tag.toString())
        }
        startActivity(intent)
    }
}
