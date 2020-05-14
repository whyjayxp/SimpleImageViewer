package com.example.simpleimageviewer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.simpleimageviewer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.searchButton.setOnClickListener { processInput(it) }
    }

    private fun processInput(view : View) {
        val iMM = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMM.hideSoftInputFromWindow(view.windowToken, 0)
        if (hasPermission()) searchPath()
    }

    private fun hasPermission() : Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            false
        } else {
            true
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

        val file = File(inputPath)
        if (inputPath == "" || !file.exists()) { // will there be SecurityException?
            binding.message.text = getString(R.string.illegal_path_error)
            binding.message.visibility = View.VISIBLE
            return
        }

        if (file.isDirectory) {
            val files = file.listFiles { f : File -> isSupportedType(f) }
            if (files == null || files.isEmpty()) { // why would files be null? (eg. storage/self)
                binding.message.text = getString(R.string.no_supported_files_error)
            } else {
                binding.message.text = (files.joinToString(separator = "\n"))
            }
            binding.message.visibility = View.VISIBLE
        } else {
            if (isSupportedType(file)) {
                // show image
                binding.message.visibility = View.GONE
                val intent = Intent(this, ImageActivity::class.java).apply {
                    putExtra("imagePath", inputPath)
                }
                startActivity(intent)
            } else {
                // show filename
                binding.message.text = file.name
                binding.message.visibility = View.VISIBLE
            }
        }
    }
}
