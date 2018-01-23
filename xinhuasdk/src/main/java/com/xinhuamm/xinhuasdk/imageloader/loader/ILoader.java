package com.xinhuamm.xinhuasdk.imageloader.loader;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.xinhuamm.xinhuasdk.imageloader.config.SingleConfig;
import com.xinhuamm.xinhuasdk.imageloader.utils.DownLoadImageService;

/**
 * Created by doudou on 2017/4/10.
 */

public interface ILoader {

//    void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD);

    void request(SingleConfig config);

    void pause();

    void resume();

    void clearDiskCache();

    void clearMemoryCache(View view);

    void clearMemory();

    void clearAllCache();

    boolean  isCached(String url);

    void trimMemory(int level);

    void onLowMemory();

    void saveImageIntoGallery(DownLoadImageService downLoadImageService);

    String getCacheSize();
}
