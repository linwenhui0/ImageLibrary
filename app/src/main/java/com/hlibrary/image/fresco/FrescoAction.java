package com.hlibrary.image.fresco;

import com.facebook.drawee.generic.RoundingParams;
import com.hlibrary.image.HImageView;

/**
 * Created by danni on 2016/7/9.
 */
public interface FrescoAction {
    /**
     * The common network loading
     */
    void loadIMG(HImageView simpleDraweeView, String url);

    /**
     * gradient loading
     */
    void loadImg(HImageView simpleDraweeView, String url);

    /**
     * loadCustomImg
     */
    void loadCustomImg(HImageView simpleDraweeView, String url, RoundingParams roundingParams, int iconFailureID, int iconPlaceHolderID);

    /**
     * loadRoundImg
     */
    void loadRoundImg(HImageView simpleDraweeView, String url);

    /**
     * loadRoundedCornerImg
     */
    void loadRoundedCornerImg(HImageView simpleDraweeView, String url);

    /**
     *  loadCustomRoundedConerImg
     */
    void loadCustomRoundedConerImg(HImageView simpleDraweeView, String url, RoundingParams roundingParams);
}
