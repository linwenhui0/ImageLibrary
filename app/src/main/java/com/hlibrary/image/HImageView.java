package com.hlibrary.image;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by linwenhui on 2016/10/26.
 */

public class HImageView extends SimpleDraweeView {
    public HImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public HImageView(Context context) {
        super(context);
    }

    public HImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
