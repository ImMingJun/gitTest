package com.shuyu.gsyvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.DanmakuVideoPlayer;

import java.lang.reflect.Constructor;


public class FullScreenActivity extends Activity {

    static void startActivityFromNormal(Context context, int state, String url, Class videoPlayClass, Object... obj) {
        CURRENT_STATE = state;
        DIRECT_FULLSCREEN = false;
        mUrl = url;
        VIDEO_PLAYER_CLASS = videoPlayClass;
        OBJECTS = obj;
        Intent intent = new Intent(context, FullScreenActivity.class);
        context.startActivity(intent);
    }

    // 直接进入全屏播放
    public static void startActivity(Context context, String url, Class videoPlayClass, Object... obj) {
        CURRENT_STATE = DanmakuVideoPlayer.CURRENT_STATE_NORMAL;
        mUrl = url;
        DIRECT_FULLSCREEN = true;
        VIDEO_PLAYER_CLASS = videoPlayClass;
        OBJECTS = obj;
        Intent intent = new Intent(context, FullScreenActivity.class);
        context.startActivity(intent);
    }

    private DanmakuVideoPlayer mBaseVideoPlayer;

    // 刚启动全屏时的播放状态
    static int CURRENT_STATE = -1;
    protected static String mUrl;
    //this is should be in videoplayer
    static boolean DIRECT_FULLSCREEN = false;
    static Class VIDEO_PLAYER_CLASS;
    static Object[] OBJECTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            Constructor<DanmakuVideoPlayer> constructor = VIDEO_PLAYER_CLASS.getConstructor(Context.class);
            mBaseVideoPlayer = constructor.newInstance(this);
            setContentView(mBaseVideoPlayer);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mBaseVideoPlayer.mIfCurrentIsFullscreen = true;
//        mBaseVideoPlayer.mIfFullscreenIsDirectly = DIRECT_FULLSCREEN;
//        mBaseVideoPlayer.setUp(mUrl, OBJECTS);
        mBaseVideoPlayer.hideDanmuku(true);
        mBaseVideoPlayer.setDanmaKuShow(false);
        mBaseVideoPlayer.setUp(mUrl,false,OBJECTS);
        mBaseVideoPlayer.setStateAndUi(CURRENT_STATE);
        mBaseVideoPlayer.addTextureView();
        mBaseVideoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {

            }

            @Override
            public void onClickStartError(String url, Object... objects) {

            }

            @Override
            public void onClickStop(String url, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {

            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                finish();
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {

            }
        });
//        if (mBaseVideoPlayer.mIfFullscreenIsDirectly) {
            mBaseVideoPlayer.mStartButton.performClick();

//        } else {
//            DanmakuVideoPlayer.IF_RELEASE_WHEN_ON_PAUSE = true;
//            SimpleMediaManager.instance().setListener(mBaseVideoPlayer);
//            if (CURRENT_STATE == BaseVideoPlayer.CURRENT_STATE_PAUSE) {
//                SimpleMediaManager.instance().mMediaPlayer.seekTo(SimpleMediaManager.instance().mMediaPlayer.getCurrentPosition());
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        finish();
        //mBaseVideoPlayer.onBackFullscreen();
//        mBaseVideoPlayer.backFullscreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DanmakuVideoPlayer.releaseAllVideos();
//        finish();
    }
}
