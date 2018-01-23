package com.transportmm.tsportapp.mvp.push;

import android.os.Parcel;

import com.xinhuamm.xinhuasdk.push.BasePushItem;

/**
 * 推送实体
 *
 */
public class PushItem extends BasePushItem{

    public int code;
    public PushChildItem contents;

    public PushItem(){

    }

    @Override
    public String getTitle() {

        return contents.pushtopic;
    }

    @Override
    public String getContent() {
        return contents.abs;
    }

    @Override
    public String getImageUrl() {
        return contents.pushimageurl;
    }

    @Override
    public boolean isMulti() {
        return contents.ismulti==1;
    }

    @Override
    public boolean isNewsNotification() {
        return code==1;
    }

    @Override
    public int getNewsType() {
        return contents.newstype;
    }

}
