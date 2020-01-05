package com.hlibrary.image.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.view.SimpleDraweeView
import com.hlibrary.image.ImageManager.Companion.getInstance
import com.hlibrary.image.R

/**
 * Created by linwenhui on 2016/10/26.
 */
class HImageView : SimpleDraweeView {
    var loadNetImage: String? = null
    var isCircle = false
    var cornersRadius = 0f

    constructor(context: Context, hierarchy: GenericDraweeHierarchy?) : super(context, hierarchy) {
        initView(context, null, 0)
    }

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) { // 获取自定义属性的值
        val a = getContext().theme.obtainStyledAttributes(attrs,
                R.styleable.HImageView, defStyleAttr, 0)
        loadNetImage = a.getString(R.styleable.HImageView_loadNetImage)
        isCircle = a.getBoolean(R.styleable.HImageView_circle, false)
        cornersRadius = a.getFloat(R.styleable.HImageView_cornersRadius, 0f)
        a.recycle()
        loadingNetImage(context)
    }

    @Synchronized
    private fun loadingNetImage(context: Context) {
        if (TextUtils.isEmpty(loadNetImage) || loadNetImage?.startsWith("http://") != true) {
            return
        }
        getInstance(context).load(this, loadNetImage!!, if (isCircle) -1f else cornersRadius, R.drawable.ic_placeholder, R.drawable.icon_failure)
    }

    fun loadingNetUrl() {
        loadingNetImage(context)
    }
}