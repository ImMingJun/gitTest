package com.xinhuamm.xinhuasdk.imageloader.loader;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.xinhuamm.xinhuasdk.imageloader.config.GlobalConfig;
import com.xinhuamm.xinhuasdk.imageloader.config.SingleConfig;
import com.xinhuamm.xinhuasdk.imageloader.utils.DownLoadImageService;


public class ImageLoader {
    public static Context context;
    /**
     * 默认最大缓存
     */
    public static int CACHE_IMAGE_SIZE = 250;

    public static void init(final Context context) {
        init(context, CACHE_IMAGE_SIZE);
    }

    public static void init(final Context context, int cacheSizeInM) {
        init(context, cacheSizeInM, MemoryCategory.NORMAL);
    }

    public static void init(final Context context, int cacheSizeInM, MemoryCategory memoryCategory) {
        init(context, cacheSizeInM, memoryCategory, true);
    }

    /**
     * @param context        上下文
     * @param cacheSizeInM   Glide默认磁盘缓存最大容量250MB
     * @param memoryCategory 调整内存缓存的大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
     * @param isInternalCD   true 磁盘缓存到应用的内部目录 / false 磁盘缓存到外部存
     */
    public static void init(final Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD) {
        if (ImageLoader.context == null) {
            ImageLoader.context = context;
            GlobalConfig.init(context, cacheSizeInM, memoryCategory, isInternalCD);
        }
    }

    /**
     * 获取当前的Loader
     *
     * @return
     */
    public static ILoader getActualLoader() {
        return GlobalConfig.getLoader();
    }

    /**
     * 加载普通图片
     *
     * @param context
     * @return
     */
    public static SingleConfig.ConfigBuilder with(Context context) {
        return new SingleConfig.ConfigBuilder(context);
    }

    public static void trimMemory(int level) {
        getActualLoader().trimMemory(level);
    }

    public static void onLowMemory() {
        getActualLoader().onLowMemory();
    }

    public static void pauseRequests() {
        getActualLoader().pause();

    }

    public static void resumeRequests() {
        getActualLoader().resume();
    }

    /**
     * Cancel any pending loads Glide may have for the view and free any resources that may have been loaded for the view.
     *
     * @param view
     */
    public static void clearMemoryCache(View view) {
        getActualLoader().clearMemoryCache(view);
    }


    /**
     * Clears disk cache.
     * <p>
     * <p>
     * This method should always be called on a background thread, since it is a blocking call.
     * </p>
     */
    public static void clearDiskCache() {
        getActualLoader().clearDiskCache();
    }

    /**
     * Clears as much memory as possible.
     */
    public static void clearMemory() {
        getActualLoader().clearMemory();
    }

    /**
     * 图片保存到相册
     *
     * @param downLoadImageService
     */
    public static void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        getActualLoader().saveImageIntoGallery(downLoadImageService);
    }

    public static String getCacheSize() {
        return getActualLoader().getCacheSize();
    }
}

