package com.transportmm.tsportapp.mvp.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.squareup.haha.perflib.Main;
import com.transportmm.tsportapp.MainActivity;
import com.xinhuamm.xinhuasdk.push.BasePushItem;
import com.xinhuamm.xinhuasdk.push.GetuiIntentService;
import com.xinhuamm.xinhuasdk.push.GetuiPushService;

import timber.log.Timber;

public class PushService extends GetuiIntentService<PushItem> {

    public PushService() {
        super();
    }

    @Override
    public BasePushItem getItemFromJson(String data) {

        Gson gson = new Gson();
        PushItem item = gson.fromJson(data, PushItem.class);
        return item;
    }

    @Override
    public String getFirstActivity() {
        return MainActivity.class.getName();
    }

    public void onReceiveClientId(Context context, String clientid) {
        super.onReceiveClientId(context,clientid);
        //写你自己的东西吧
    }
}
