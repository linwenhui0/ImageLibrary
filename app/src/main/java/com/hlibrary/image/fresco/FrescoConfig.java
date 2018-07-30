package com.hlibrary.image.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hlibrary.image.view.HImageView;
import com.hlibrary.image.R;
import com.hlibrary.image.fresco.controller.CalHeightController;
import com.hlibrary.util.SDUtil;
import com.hlibrary.util.file.FileManager;

import java.io.File;


/**
 * Created by danni on 2016/7/9.
 */
public class FrescoConfig {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//使用的缓存数量

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;//默认图磁盘缓存的最大值


    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "imagepipeline_cache";//小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";//默认图所放路径的文件夹名

    private static ImagePipelineConfig sImagePipelineConfig;


    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = configureCaches(context);
        }
        return sImagePipelineConfig;
    }


    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static ImagePipelineConfig configureCaches(Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                FrescoConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                FrescoConfig.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size

        //修改内存图片缓存数量
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig;

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig;
        if (!SDUtil.ExistSDCard()) {
            diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(new File(FileManager.getPictureMinCachePath(context)))
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)
                    .build();
            diskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(new File(FileManager.getPictureCachePath(context)))
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)
                    .build();
        } else {
            diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(context.getCacheDir())
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)
                    .build();
            diskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(context.getCacheDir())
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)
                    .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)
                    .build();
        }
        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig).setBitmapsConfig(Bitmap.Config.RGB_565);
        return configBuilder.build();
    }

    /**
     * DraweeHierarchy
     *
     * @param context
     * @return
     */
    public static GenericDraweeHierarchy getGenericDraweeHierarchy(Context context) {

//        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(context.getResources())
//                .setFailureImage(ContextCompat.getDrawable(context, R.drawable.icon_failure), ScalingUtils.ScaleType.FIT_XY)//fresco:failureImage="@drawable/error"失败图
//                .setPlaceholderImage(ContextCompat.getDrawable(context, R.drawable.ic_placeholder), ScalingUtils.ScaleType.FIT_XY)//fresco:placeholderImage="@color/wait_color"占位图
//            .reset()//重置
//            .setActualImageColorFilter(colorFilter)//颜色过滤
//            .setActualImageFocusPoint(focusPoint)//focusCrop, 需要指定一个居中点
//            .setActualImageMatrix(actualImageMatrix)
//            .setActualImageScaleType(actualImageScaleType)//fresco:actualImageScaleType="focusCrop"缩放类型
//            .setBackground(background)//fresco:backgroundImage="@color/blue"背景图片
//            .setBackgrounds(backgrounds)
//            .setFadeDuration(fadeDuration)//fresco:fadeDuration="300"加载图片动画时间
//            .setOverlay(overlay)//fresco:overlayImage="@drawable/watermark"叠加图
//            .setOverlays(overlays)
//            .setPressedStateOverlay(drawable)//fresco:pressedStateOverlayImage="@color/red"按压状态下的叠加图
//            .setProgressBarImage(new ProgressBarDrawable())//进度条fresco:progressBarImage="@drawable/progress_bar"进度条
//            .setProgressBarImage(progressBarImage, progressBarImageScaleType)//fresco:progressBarImageScaleType="centerInside"进度条类型
//            .setRetryImage(retryDrawable)//fresco:retryImage="@drawable/retrying"点击重新加载
//            .setRetryImage(retryDrawable, retryImageScaleType)//fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
//            .setRoundingParams(RoundingParams.asCircle())//圆形/圆角fresco:roundAsCircle="true"圆形
//                .build();
        return getCustomPlaceholderGenericDraweeHierarchy(context, null, R.drawable.ic_placeholder, R.drawable.icon_failure);
    }

    /**
     * rounded corner
     */
    public static GenericDraweeHierarchy getRoundedCornerGenericDraweeHierarchy(Context context, RoundingParams roundingParams) {
        return getCustomPlaceholderGenericDraweeHierarchy(context, roundingParams, 0, 0);
    }

    /**
     * Custom   placeholder  failureImg
     */
    public static GenericDraweeHierarchy getCustomPlaceholderGenericDraweeHierarchy(Context context, RoundingParams roundingParams, int iconPlaceHolderID, int iconFailureId) {
        GenericDraweeHierarchyBuilder gdhBuilder = new GenericDraweeHierarchyBuilder(context.getResources());
        gdhBuilder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        if (roundingParams != null)
            gdhBuilder.setRoundingParams(roundingParams);
        if (iconFailureId > 0)
            gdhBuilder.setFailureImage(ContextCompat.getDrawable(context, iconFailureId), ScalingUtils.ScaleType.FIT_CENTER);
        if (iconPlaceHolderID > 0)
            gdhBuilder.setPlaceholderImage(ContextCompat.getDrawable(context, iconPlaceHolderID), ScalingUtils.ScaleType.FIT_CENTER);
        return gdhBuilder.setFadeDuration(1000).build();
    }

    public static DraweeController getDraweeController(HImageView imageView, String url) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .setControllerListener(new CalHeightController(imageView))
                .build();
        return draweeController;
    }

    public static DraweeController getDraweeController(HImageView imageView, File file) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.fromFile(file))
                .setAutoPlayAnimations(true)
                .setControllerListener(new CalHeightController(imageView))
                .build();
        return draweeController;
    }


    /**
     * rounded corner params
     * fromCornersRadius(float radius)  设置圆角半径
     * fromCornersRadii(float[] radii) 分别设置左上角、右上角、左下角、右下角圆角半径
     * fromCornersRadii(float topLeft, float topRight, float bottomRight, float bottomLeft)  分别设置左上角、右上角、左下角、右下角圆角半径
     * setCornersRadius(float radius)  设置圆角半径
     * setCornersRadii(float[] radii) 分别设置左上角、右上角、左下角、右下角圆角半径
     * setCornersRadii(float topLeft, float topRight, float bottomRight, float bottomLeft)  分别设置左上角、右上角、左下角、右下角圆角半径
     *
     * @return
     */
    public static RoundingParams getRoundingParams() {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(30);
//    roundingParams.asCircle();//圆形
//    roundingParams.setBorder(color, width);//fresco:roundingBorderWidth="2dp"边框  fresco:roundingBorderColor="@color/border_color"
//    roundingParams.setCornersRadii(radii);//半径
//    roundingParams.setCornersRadii(topLeft, topRight, bottomRight, bottomLeft)//fresco:roundTopLeft="true" fresco:roundTopRight="false" fresco:roundBottomLeft="false" fresco:roundBottomRight="true"
//    roundingParams. setCornersRadius(radius);//fresco:roundedCornerRadius="1dp"圆角
//    roundingParams.setOverlayColor(overlayColor);//fresco:roundWithOverlayColor="@color/corner_color"
//    roundingParams.setRoundAsCircle(roundAsCircle);//圆
//    roundingParams.setRoundingMethod(roundingMethod);
//    fresco:progressBarAutoRotateInterval="1000"自动旋转间隔
        // 或用 fromCornersRadii 以及 asCircle 方法
        return roundingParams;
    }

    /**
     * You can initialize the global configuration picture
     */
//    public static Drawable sPlaceholderDrawable;
//    public static Drawable sErrorDrawable;
//    @SuppressWarnings("deprecation")
//    public static void init(final Resources resources) {
//        if (sPlaceholderDrawable == null) {
//            sPlaceholderDrawable = resources.getDrawable(R.mipmap.ic_placeholder);
//        }
//        if (sErrorDrawable == null) {
//            sErrorDrawable = resources.getDrawable(R.mipmap.icon_failure);
//        }
//    }

    /**
     * Picture decoding
     *
     * @return
     */
    public static ImageDecodeOptions getImageDecodeOptions() {
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
//            .setBackgroundColor(Color.TRANSPARENT)//图片的背景颜色
//            .setDecodeAllFrames(decodeAllFrames)//解码所有帧
//            .setDecodePreviewFrame(decodePreviewFrame)//解码预览框
//            .setForceOldAnimationCode(forceOldAnimationCode)//使用以前动画
//            .setFrom(options)//使用已经存在的图像解码
//            .setMinDecodeIntervalMs(intervalMs)//最小解码间隔（分位单位）
                .setUseLastFrameForPreview(true)//使用最后一帧进行预览
                .build();
        return decodeOptions;
    }

    /**
     * imageRequest
     *
     * @param view
     * @param uri
     * @return
     */
    public static ImageRequest getImageRequest(DraweeView view, String uri) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//            .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)//请求经过缓存级别  BITMAP_MEMORY_CACHE，ENCODED_MEMORY_CACHE，DISK_CACHE，FULL_FETCH
//            .setAutoRotateEnabled(true)//自动旋转图片方向
//            .setImageDecodeOptions(getImageDecodeOptions())//  图片解码库
//            .setImageType(ImageType.SMALL)//图片类型，设置后可调整图片放入小图磁盘空间还是默认图片磁盘空间
//            .setLocalThumbnailPreviewsEnabled(true)//缩略图预览，影响图片显示速度（轻微）
//            .setPostprocessor(postprocessor)//修改图片
//            .setProgressiveRenderingEnabled(true)//渐进加载，主要用于渐进式的JPEG图，影响图片显示速度（普通）
//            .setSource(Uri uri)//设置图片地址
                .setResizeOptions(new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))//调整大小
                .build();
        return imageRequest;
    }

    /**
     * DraweeController params
     *
     * @param imageRequest
     * @param view
     * @return
     */
    public static DraweeController getDraweeController(ImageRequest imageRequest, DraweeView view) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)//自动播放图片动画
                .setImageRequest(imageRequest)//设置单个图片请求～～～不可与setFirstAvailableImageRequests共用，配合setLowResImageRequest为高分辨率的图
                .setOldController(view.getController())//DraweeController复用
                .setTapToRetryEnabled(true)//点击重新加载图
//            .setCallerContext(callerContext)//回调
//            .reset()//重置
//            .setControllerListener(view.getListener())//监听图片下载完毕等
//            .setDataSourceSupplier(dataSourceSupplier)//数据源
//            .setFirstAvailableImageRequests(firstAvailableImageRequests)//本地图片复用，可加入ImageRequest数组
//            .setLowResImageRequest(ImageRequest.fromUri(lowResUri))//先下载显示低分辨率的图
                .build();
        return draweeController;
    }
}
