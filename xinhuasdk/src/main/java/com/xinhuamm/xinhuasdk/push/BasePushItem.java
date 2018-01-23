package com.xinhuamm.xinhuasdk.push;

import android.os.Parcelable;

import java.io.Serializable;

public abstract class BasePushItem implements Serializable{


    /**
     * 标题
     * @return
     */
    public abstract String getTitle();

    /**
     * 内容
     * @return
     */
    public abstract String getContent();

    /**
     * 图片的地址
     * @return
     */
    public abstract String getImageUrl();


    /**
     * 是否一个推送信息包含多个稿件，true表示包含多个稿件，跳转到消息中心，false表示点击跳转到相应的稿件
     * @return
     */
    public abstract boolean isMulti();

    /**
     * 是通知推送还是新闻推送，true表示新闻推送，点击跳转到相应的稿件界面，false表示点击打开软件
     * @return
     */
    public abstract boolean isNewsNotification();


    /**
     * 稿件类型
     * @return
     */
    public abstract int getNewsType();
}
