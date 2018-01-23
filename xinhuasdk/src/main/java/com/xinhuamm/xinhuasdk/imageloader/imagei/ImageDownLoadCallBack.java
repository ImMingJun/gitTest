package com.xinhuamm.xinhuasdk.imageloader.imagei;

import android.graphics.Bitmap;


public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
