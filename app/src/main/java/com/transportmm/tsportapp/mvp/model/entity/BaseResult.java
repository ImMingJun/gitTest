package com.transportmm.tsportapp.mvp.model.entity;

import android.os.Parcel;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseResult<T> {
    public final static int SUCC_TAG = 0, FAIL_TAG = -1;  // 0成功标志， -1 失败标志
    private T data;
    private int state;
    private String message;
    private int hasmore;//0表示没有更多，1有更多

    protected BaseResult(Parcel in) {
        state = in.readInt();
        message = in.readString();
        hasmore = in.readInt();
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
      return state==SUCC_TAG;
    }

}
