/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xinhuamm.xinhuasdk;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.luck.picture.lib.loader.IPictrureMediaLoader;
import com.luck.picture.lib.loader.IPictureCallback;
import com.xinhuamm.xinhuasdk.imageloader.config.DiskCacheStrategyMode;
import com.xinhuamm.xinhuasdk.imageloader.config.PriorityMode;
import com.xinhuamm.xinhuasdk.imageloader.config.ScaleMode;
import com.xinhuamm.xinhuasdk.imageloader.config.SingleConfig;
import com.xinhuamm.xinhuasdk.imageloader.loader.ImageLoader;

/**
 * use https://github.com/bumptech/glide as media loader.
 * can <b>not</b> be used in Production Environment.
 */
public class PictrureGlideLoader implements IPictrureMediaLoader {

    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {

        ImageLoader.with(img.getContext())
                .url(absPath)
                .placeHolder(R.drawable.image_placeholder)
                .asBitmap()
                .scale(ScaleMode.CENTER_CROP)
                .diskCacheStrategy(DiskCacheStrategyMode.NONE)
                .override(width, height)
                .into(img);


    }

    @Override
    public void displayRaw(@NonNull final ImageView img, @NonNull String absPath, int width, int height, boolean isGif, final IPictureCallback callback) {

        if (isGif) {
            ImageLoader.with(img.getContext())
                    .url(absPath)
                    .asGif()
                    .override(width, height)
                    .priority(PriorityMode.PRIORITY_HIGH)
                    .diskCacheStrategy(DiskCacheStrategyMode.NONE)
                    .listener(new SingleConfig.RequestListener() {
                        @Override
                        public void onSuccess() {
                            if (callback != null)
                                callback.onSuccess();
                        }

                        @Override
                        public void onFail() {
                            if (callback != null)
                                callback.onFail(null);
                        }
                    })
                    .into(img);

        } else {
            ImageLoader.with(img.getContext())
                    .url(absPath)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategyMode.NONE)
                    .listener(new SingleConfig.RequestListener() {
                        @Override
                        public void onSuccess() {
                            if (callback != null)
                                callback.onSuccess();
                        }

                        @Override
                        public void onFail() {
                            if (callback != null)
                                callback.onFail(null);
                        }
                    })
                    .into(img);
        }

    }

}
