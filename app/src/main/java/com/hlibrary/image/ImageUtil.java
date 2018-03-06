package com.hlibrary.image;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.hlibrary.image.fresco.FrescoConfig;
import com.hlibrary.image.fresco.ImageManager;
import com.hlibrary.util.Logger;


public class ImageUtil {

    private static final String TAG = "ImageUtil";


    public static void initImageLoader(Context context) {
        Fresco.initialize(context, FrescoConfig.getImagePipelineConfig(context));
    }

    /**
     * @说明：普通ImageView加载图片
     */
    public static void displayBitmap(Context context, String url, HImageView imgvw) {
        ImageManager.getFrescoAction(context).loadImg(imgvw, url);
    }

    public static void displayBitmap(Context context, RoundingParams imageStyle, String url, HImageView imgvw) {
        ImageManager.getFrescoAction(context).loadCustomRoundedConerImg(imgvw, url, imageStyle);
    }

    /**
     * @说明：普通ImageView加载图片
     */
    public static void displayBitmap(Context context, int emptyRes, int failureRes,
                                     RoundingParams imageStyle, String url, HImageView imgvw) {
        Logger.getInstance().i(TAG, url);
        ImageManager.getFrescoAction(context).loadCustomImg(imgvw, url, imageStyle, failureRes, emptyRes);
    }

    /**
     * @说明：ListView中Adapter中存在ImageView的加载图片
     */
    public static void displayBitmap(Context context,String url, HImageView imgvw, ListView lstvw) {
        lstvw.setOnScrollListener(new AdapterScrollImp());
        ImageManager.getFrescoAction(context).loadImg(imgvw, url);
    }

    public static void cancelDisplay(HImageView imgvw) {
        imgvw.shutDown();
    }

    public static void clearMemory() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    public static boolean checkInited(Context context) {
        return Fresco.hasBeenInitialized();
    }

    private static class AdapterScrollImp implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            switch (scrollState) {

                case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                    if (Fresco.getImagePipeline().isPaused())
                        Fresco.getImagePipeline().resume();
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                case OnScrollListener.SCROLL_STATE_FLING://滚动状态
                    if (!Fresco.getImagePipeline().isPaused())
                        Fresco.getImagePipeline().pause();
                    break;

            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


}
