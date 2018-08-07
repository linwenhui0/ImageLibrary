package com.hlibrary.image.listener

import android.graphics.Bitmap
import java.io.File

interface IImageDownListener {

    fun onDownFinish(bitmap: Bitmap)

    fun onDownFinish(file: File)

    fun onError(msg: String)

}