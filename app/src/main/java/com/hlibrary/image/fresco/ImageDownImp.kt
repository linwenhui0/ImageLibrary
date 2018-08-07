package com.hlibrary.image.fresco

import android.graphics.Bitmap
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.hlibrary.image.ImageManager
import com.hlibrary.image.listener.IImageDownListener
import com.hlibrary.util.Logger
import java.io.File


class ImageDownImp : BaseBitmapDataSubscriber {

    private var imageDownListener: IImageDownListener? = null
    private var imageRequest: ImageRequest? = null

    constructor(imageRequest: ImageRequest, imageDownListener: IImageDownListener) : super() {
        this.imageRequest = imageRequest
        this.imageDownListener = imageDownListener
    }

    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
        imageDownListener?.onError("下载照片失败")
    }


    override fun onNewResultImpl(bitmap: Bitmap?) {
        if (bitmap == null) {
            val cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(imageRequest, this)
            val resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
                    ?: return
            val file: File = (resource as FileBinaryResource).file ?: return
            if (ImageManager.debug)
                Logger.getInstance().defaultTagD("onNewResultImpl bitmap = ", bitmap != null)
            imageDownListener?.onDownFinish(file)
        } else {
            imageDownListener?.onDownFinish(bitmap)
        }

    }


}