package com.hlibrary.image.fresco;

import android.content.Context;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.hlibrary.image.HImageView;


/**
 * Created by danni on 2016/7/9.
 */
public class FrescoImp implements FrescoAction {

    private Context context;

    public FrescoImp(Context context) {
        this.context = context;
    }

    /**
     * the common Image loading
     *
     * @param simpleDraweeView
     * @param url
     */
    @Override
    public void loadIMG(HImageView simpleDraweeView, String url) {
        //Two basic methods for loading pictures
        //1,SimpleDraweeView this control, and the picture shows the time to write a direct setImageURI (URI)
//        simpleDraweeView.setImageURI(Uri.parse(url));
        //2,It also provides setController (controller) method to load the image
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
    }

    /**
     * the common Image loading  but added  default failure picture and place holder picture
     *
     * @param simpleDraweeView
     * @param url
     */
    @Override
    public void loadImg(HImageView simpleDraweeView, String url) {
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
        simpleDraweeView.setHierarchy(FrescoConfig.getGenericDraweeHierarchy(context));
    }

    /**
     * Custom loading pictures
     *
     * @param simpleDraweeView
     * @param url
     * @param iconPlaceHolderID
     * @param iconFailureId
     */
    @Override
    public void loadCustomImg(HImageView simpleDraweeView, String url, RoundingParams roundingParams, int iconPlaceHolderID, int iconFailureId) {
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
        simpleDraweeView.setHierarchy(FrescoConfig.getCustomPlaceholderGenericDraweeHierarchy(context, roundingParams,
                iconPlaceHolderID, iconFailureId));
    }

    // refer http://blog.csdn.net/y1scp/article/details/49734429
    @Override
    public void loadRoundImg(HImageView simpleDraweeView, String url) {
        RoundingParams roundingParams = RoundingParams.asCircle();
        GenericDraweeHierarchy hierarchy = FrescoConfig.getRoundedCornerGenericDraweeHierarchy(context, roundingParams);
        simpleDraweeView.setHierarchy(hierarchy);
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
    }

    @Override
    public void loadRoundedCornerImg(HImageView simpleDraweeView, String url) {
        RoundingParams roundingParams = FrescoConfig.getRoundingParams();
        GenericDraweeHierarchy hierarchy = FrescoConfig.getRoundedCornerGenericDraweeHierarchy(context, roundingParams);
        simpleDraweeView.setHierarchy(hierarchy);
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
    }

    @Override
    public void loadCustomRoundedConerImg(HImageView simpleDraweeView, String url, RoundingParams roundingParams) {
        GenericDraweeHierarchy hierarchy = FrescoConfig.getRoundedCornerGenericDraweeHierarchy(context, roundingParams);
        simpleDraweeView.setHierarchy(hierarchy);
        DraweeController controller = FrescoConfig.getDraweeController(url);
        simpleDraweeView.setController(controller);
    }
}
