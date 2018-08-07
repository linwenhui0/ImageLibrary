package com.hlibrary.image.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlibrary.image.ImageManager;
import com.hlibrary.image.R;

/**
 * Created by linwenhui on 2016/10/26.
 */

public class HImageView extends SimpleDraweeView {

    private String loadNetImage;
    private boolean circle;
    private float cornersRadius;

    public HImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        initView(context, null, 0);
    }

    public HImageView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public HImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public HImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, 0);
    }

    public HImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        // 获取自定义属性的值
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.HImageView, defStyleAttr, 0);

        loadNetImage = a.getString(R.styleable.HImageView_loadNetImage);
        circle = a.getBoolean(R.styleable.HImageView_circle, false);
        cornersRadius = a.getFloat(R.styleable.HImageView_cornersRadius, 0f);
        a.recycle();
        loadingNetImage(context);
    }

    private synchronized void loadingNetImage(Context context) {
        if (TextUtils.isEmpty(loadNetImage) || !loadNetImage.startsWith("http://"))
            return;
        ImageManager.Companion.getInstance(context).load(this, loadNetImage, circle ? -1f : cornersRadius, R.drawable.ic_placeholder, R.drawable.icon_failure);
    }

    public boolean isCircle() {
        return circle;
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    public float getCornersRadius() {
        return cornersRadius;
    }

    public void setCornersRadius(float cornersRadius) {
        this.cornersRadius = cornersRadius;
    }

    public String getLoadNetImage() {
        return loadNetImage;
    }

    public void setLoadNetImage(String loadNetImage) {
        this.loadNetImage = loadNetImage;
    }

    public void loadingNetUrl() {
        loadingNetImage(getContext());
    }

}
