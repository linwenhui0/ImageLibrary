package com.hlibrary.image.listener

import android.view.View
import com.hlibrary.image.R
import java.io.File

interface IImageAccessor {

    fun init()

    fun hasBeenInit(): Boolean

    fun load(v: View, url: String, cornersRadius: Float = 0f, emptyRes: Int = R.drawable.ic_placeholder, failureRes: Int = R.drawable.icon_failure): Boolean

    fun load(v: View, file: File): Boolean

    fun load(url: String, obj: Any, imageDownListener: IImageDownListener)

    fun clearMemoryCache()
}