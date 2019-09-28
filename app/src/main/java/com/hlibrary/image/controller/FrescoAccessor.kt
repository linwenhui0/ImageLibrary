package com.hlibrary.image.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.DraweeHolder
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.hlibrary.image.fresco.FrescoConfig
import com.hlibrary.image.fresco.ImageDownImp
import com.hlibrary.image.listener.IImageAccessor
import com.hlibrary.image.listener.IImageDownListener
import com.hlibrary.image.view.HImageView
import com.hlibrary.util.Logger
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
        Logger.instance.defaultTagD("url = $url , view = ${v.toString()}")
        if (TextUtils.isEmpty(url))
            return false
        if (v is HImageView) {
            val controller = FrescoConfig.getDraweeController(v, url)
            v.controller = controller
            var roundingParams = RoundingParams()
            if (cornersRadius < 0) {
                roundingParams.setRoundAsCircle(true)
            } else {
                roundingParams.setRoundAsCircle(false)
                roundingParams.setCornersRadius(cornersRadius)
            }
            v.hierarchy = FrescoConfig.getCustomPlaceholderGenericDraweeHierarchy(context, roundingParams,
                    emptyRes, failureRes)
            return true
        }
        load(url, v, object : IImageDownListener {
            override fun onDownFinish(bitmap: Bitmap) {
                if (v is ImageView) {
                    v.setImageBitmap(bitmap)
                } else {
                    v.setBackgroundDrawable(BitmapDrawable(v.context.resources, bitmap))
                }
            }

            override fun onDownFinish(file: File) {
                var bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (v is ImageView) {
                    v.setImageBitmap(bitmap)
                } else {
                    v.setBackgroundDrawable(BitmapDrawable(v.context.resources, bitmap))
                }
            }

            override fun onError(msg: String) {
                if (v is ImageView) {
                    v.setImageResource(failureRes)
                } else {
                    v.setBackgroundResource(failureRes)
                }
            }
        })
        return false
    }

    override fun load(v: View, file: File): Boolean {
        Logger.instance.defaultTagD("url = ${file.absolutePath} , view = ${v.toString()}")
        if (!file.exists())
            return false
        if (v is HImageView) {
            val controller = FrescoConfig.getDraweeController(v, file)
            v.controller = controller
            v.hierarchy = FrescoConfig.getGenericDraweeHierarchy(context)
            return true
        }
        var bitmap = BitmapFactory.decodeFile(file.absolutePath)
        if (v is ImageView) {
            v.setImageBitmap(bitmap)
        } else {
            v.setBackgroundDrawable(BitmapDrawable(v.context.resources, bitmap))
        }
        return false
    }

    override fun load(url: String, obj: Any, imageDownListener: IImageDownListener) {

        Logger.instance.defaultTagD("url = $url")
        val uri = Uri.parse(url)
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true).build()
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(imageRequest, this)
        dataSource.subscribe(ImageDownImp(imageRequest, imageDownListener),
                UiThreadImmediateExecutorService.getInstance())
        var hierarchy = FrescoConfig.getGenericDraweeHierarchy(context)
        var draweeHolder = DraweeHolder.create(hierarchy, context)
        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.controller)
                .setImageRequest(imageRequest)
                .build()
        controller.onClick()

    }

    override fun clearMemoryCache() {
        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches()
    }
}