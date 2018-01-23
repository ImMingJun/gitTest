package com.transportmm.tsportapp.mvp.push;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 推送实体子类
 */
public class PushChildItem implements Serializable{
    public long id;
    public int showType;  //暂时没有使用
    public int o;  //  对应 @See NewsEntity opentype
    public int newstype;
    public String abs;
    public String pushimageurl; //图片地址
    public int ismulti;//0不是组推   1 组推
    public String pushtopic;

    public PushChildItem() {

    }
}
