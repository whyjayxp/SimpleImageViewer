package com.example.simpleimageviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageGridViewModel : ViewModel() {
    val imageList = MutableLiveData<Array<String>>()

    init {
        imageList.value = arrayOf()
    }
}