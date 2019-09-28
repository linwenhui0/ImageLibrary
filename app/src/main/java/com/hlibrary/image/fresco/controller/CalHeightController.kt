package com.hlibrary.image.fresco.controller

import android.graphics.drawable.Animatable
import android.view.ViewGroup
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.hlibrary.image.view.HImageView
import com.hlibrary.util.Logger

class CalHeightController : BaseControllerListener<ImageInfo> {
    var view: HImageView? = null

    constructor(view: HImageView) : super() {
        this.view = view
    }

    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        super.onFinalImageSet(id, imageInfo, animatable)
        if (imageInfo == null)
            return
        Logger.instance.defaultTagD("imageInfo , width : ", imageInfo.width, " , heigth = ", imageInfo.height)
        var layoutParams = view?.layoutParams
        if (layoutParams != null && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            var widgetWidth = view?.width!!
            if (widgetWidth <= 0)
                widgetWidth = view?.measuredWidth!!
            if (widgetWidth == 0)
                return
            var height: Int = imageInfo.height
            var width: Int = imageInfo.width
            layoutParams.width = widgetWidth
            layoutParams.height = ((widgetWidth * height).toFloat() / width).toInt()

            Logger.instance.defaultTagD("view layout , width : ", layoutParams.width, " , heigth = ", layoutParams.height)
            view?.layoutParams = layoutParams
        }
    }
}
