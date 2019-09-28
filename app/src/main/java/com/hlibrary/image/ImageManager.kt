package com.hlibrary.image

import android.content.Context
import android.view.View
import com.hlibrary.image.controller.FrescoAccessor
import com.hlibrary.image.listener.IImageAccessor
import com.hlibrary.image.listener.IImageDownListener
import java.io.File

class ImageManager : IImageAccessor {


    private var imageAccessor: IImageAccessor? = null

    private constructor(context: Context) {
        imageAccessor = FrescoAccessor(context)
    }

    companion object {
        private var instance: ImageManager? = null
        fun getInstance(context: Context): ImageManager {
            if (instance == null) {
                synchronized(ImageManager::class) {
                    if (instance == null)
                        instance = ImageManager(context)
                }
            }
            return instance!!
        }

    }

    override fun init() {
        if (!hasBeenInit())
            imageAccessor?.init()
    }

    override fun hasBeenInit(): Boolean {
        return imageAccessor?.hasBeenInit()!!
    }

    override fun load(v: View, url: String, cornersRadius: Float, emptyRes: Int, failureRes: Int): Boolean {
        return imageAccessor?.load(v, url, cornersRadius, emptyRes, failureRes)!!
    }

    override fun load(v: View, file: File): Boolean {
        return imageAccessor?.load(v, file)!!
    }

    override fun load(url: String, obj: Any, imageDownListener: IImageDownListener) {
        imageAccessor?.load(url, obj, imageDownListener)
    }

    override fun clearMemoryCache() {
        imageAccessor?.clearMemoryCache()
    }

}