package com.hlibrary.image.controller

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.hlibrary.image.fresco.FrescoConfig
import com.hlibrary.image.listener.IImageAccessor
import com.hlibrary.image.view.HImageView
import java.io.File

class FrescoAccessor : IImageAccessor {

    private var context: Context? = null

    constructor(context: Context) {
        this.context = context.applicationContext
    }

    override fun init() {
        Fresco.initialize(context, FrescoConfig.getImagePipelineConfig(context))
    }

    override fun hasBeenInit(): Boolean {
        return Fresco.hasBeenInitialized()
    }

    override fun load(v: View, url: String, cornersRadius: Float, emptyRes: Int, failureRes: Int): Boolean {
        if (TextUtils.isEmpty(url))
            return false
        if (v is HImageView) {
            val controller = FrescoConfig.getDraweeController(v, url)
            v.controller = controller
            var roundingParams = RoundingParams()
            if (cornersRadius > 0)
                roundingParams.setCornersRadius(cornersRadius)

            v.hierarchy = FrescoConfig.getCustomPlaceholderGenericDraweeHierarchy(context, roundingParams,
                    emptyRes, failureRes)
            return true
        }
        return false
    }

    override fun load(v: View, file: File): Boolean {
        if (!file.exists())
            return false
        if (v is HImageView) {
            val controller = FrescoConfig.getDraweeController(v, file)
            v.controller = controller
            v.hierarchy = FrescoConfig.getGenericDraweeHierarchy(context)
            return true
        }
        return false
    }
}