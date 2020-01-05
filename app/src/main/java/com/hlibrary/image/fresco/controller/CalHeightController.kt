package com.hlibrary.image.fresco.controller

import android.graphics.drawable.Animatable
import android.view.ViewGroup
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.hlibrary.image.view.HImageView
import com.hlibrary.util.Logger

class CalHeightController(view: HImageView) : BaseControllerListener<ImageInfo>() {
    var view: HImageView? = view

    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        super.onFinalImageSet(id, imageInfo, animatable)
        if (imageInfo == null)
            return
        val layoutParams = view?.layoutParams
        if (layoutParams?.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            var widgetWidth = view?.width!!
            if (widgetWidth <= 0)
                widgetWidth = view?.measuredWidth!!
            if (widgetWidth == 0)
                return
            val height: Int = imageInfo.height
            val width: Int = imageInfo.width
            layoutParams.width = widgetWidth
            layoutParams.height = ((widgetWidth * height).toFloat() / width).toInt()

            view?.layoutParams = layoutParams
        }
    }
}
