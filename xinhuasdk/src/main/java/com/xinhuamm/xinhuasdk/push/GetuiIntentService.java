package com.xinhuamm.xinhuasdk.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.igexin.sdk.message.UnBindAliasCmdMessage;
import com.xinhuamm.xinhuasdk.R;
import com.xinhuamm.xinhuasdk.imageloader.config.GlideApp;
import com.xinhuamm.xinhuasdk.imageloader.config.GlobalConfig;
import com.xinhuamm.xinhuasdk.imageloader.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import zlc.season.rxdownload2.RxDownload;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public abstract class GetuiIntentService<T extends BasePushItem> extends GTIntentService {

    public final static String KEY_PUSH_ITEM = "KEY_PUSH_ITEM";
    public final static String KEY_TASK_ID = "KEY_TASK_ID";
    public final static String KEY_MESSAGE_ID = "KEY_MESSAGE_ID";

    public final static int PUSH_INF_ARRIVE = 90001;
    public final static int PUSH_INF_CLICK = 90002;

    public static final String TAG = GetuiPushService.class.getName();

    private NotificationManager mNotificationManager;
    private int mNotificationId = 1000;

    public GetuiIntentService() {


    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    /**
     * 获取透传数据
     *
     * @param context
     * @param msg
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, PUSH_INF_ARRIVE);
        Timber.tag(TAG).w("call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Timber.tag(TAG).w("onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Timber.tag(TAG).w("receiver payload = " + data);

            BasePushItem item = getItemFromJson(data);

            notification(item, taskid, messageid);
        }

    }

    public abstract BasePushItem getItemFromJson(String data);

    public abstract String getFirstActivity();

    public void notification(BasePushItem item, String taskId, String messageId) {

        mNotificationId = hashCode();
        Intent intentsub = new Intent();
        intentsub.setComponent(new ComponentName(getPackageName(), getFirstActivity()));
        intentsub.setAction(Intent.ACTION_VIEW);
        intentsub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intentsub.putExtra(KEY_PUSH_ITEM, item);
        intentsub.putExtra(KEY_TASK_ID, taskId);
        intentsub.putExtra(KEY_MESSAGE_ID, messageId);
        //PendingIntent.FLAG_UPDATE_CURRENT每次都更新 注:requestCode必须有(并且不能一样)不然每次返回的数据都会一样
        PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationId, intentsub, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = TextUtils.isEmpty(item.getTitle()) ? getResources().getString(R.string.app_name) : item.getTitle();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setCustomContentView(contentView)//在oppor r9m上无法展示出自定义的布局，包含CustomBigContentView，BigTextStyle，BigPictureStyle等
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setCustomBigContentView(contentView)
                .setTicker(TextUtils.isEmpty(item.getContent()) ? item.getTitle() : item.getContent())
                .setContentTitle(title)
                .setContentText(item.getContent())
                .setAutoCancel(true)
//                .setDefaults(isCloseVoice(turnOnSilentMode) ? 0 : Notification.DEFAULT_SOUND)//展示不加，后续补上
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); //让通知的标题内容在锁屏情况下也能够展示

        if (TextUtils.isEmpty(item.getImageUrl())) {//没有图片的情况下
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.bigText(item.getContent());
            style.setBigContentTitle(title);
            //SummaryText没什么用 可以不设置
            style.setSummaryText(item.getContent());
            mBuilder.setStyle(style);
        } else {
            Bitmap bitmap = null;
            try {
                bitmap = GlideApp.with(this)
                        .asBitmap()
                        .load(item.getImageUrl())
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                mBuilder.setLargeIcon(bitmap);
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                style.setBigContentTitle(title);
                style.setSummaryText(item.getContent());
                style.bigPicture(bitmap);
                mBuilder.setStyle(style);
            }

        }
        mNotificationManager.notify(mNotificationId, mBuilder.build());

    }

    /**
     * 获取ClientID
     * 第三方应用需要将CID上传到第三方服务器,
     * 并且将当前用户账号和CID进行关联,
     * 以便日后通过用户账号查找CID进行消息推送
     *
     * @param context
     * @param clientid
     */
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Timber.tag(TAG).w("onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Timber.tag(TAG).w("onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Timber.tag(TAG).w("onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if (action == PushConsts.BIND_ALIAS_RESULT) {
            bindAliasResult((BindAliasCmdMessage) cmdMessage);
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {
            unbindAliasResult((UnBindAliasCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        int text = R.string.add_tag_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = R.string.add_tag_success;
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = R.string.add_tag_error_count;
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = R.string.add_tag_error_frequency;
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = R.string.add_tag_error_repeat;
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = R.string.add_tag_error_unbind;
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = R.string.add_tag_unknown_exception;
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = R.string.add_tag_error_null;
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = R.string.add_tag_error_not_online;
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = R.string.add_tag_error_black_list;
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = R.string.add_tag_error_exceed;
                break;

            default:
                break;
        }

        Timber.tag(TAG).w("settag result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }

    private void bindAliasResult(BindAliasCmdMessage bindAliasCmdMessage) {
        String sn = bindAliasCmdMessage.getSn();
        String code = bindAliasCmdMessage.getCode();

        int text = R.string.bind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.BIND_ALIAS_SUCCESS:
                text = R.string.bind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.bind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.bind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.bind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.bind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.bind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.bind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.bind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.bind_alias_error_sn_invalid;
                break;
            default:
                break;

        }

        Timber.tag(TAG).w("bindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));

    }

    private void unbindAliasResult(UnBindAliasCmdMessage unBindAliasCmdMessage) {
        String sn = unBindAliasCmdMessage.getSn();
        String code = unBindAliasCmdMessage.getCode();

        int text = R.string.unbind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.UNBIND_ALIAS_SUCCESS:
                text = R.string.unbind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.unbind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.unbind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.unbind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.unbind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.unbind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.unbind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.unbind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.unbind_alias_error_sn_invalid;
                break;
            default:
                break;

        }

        Timber.tag(TAG).w("unbindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));

    }


    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Timber.tag(TAG).w("onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

}
