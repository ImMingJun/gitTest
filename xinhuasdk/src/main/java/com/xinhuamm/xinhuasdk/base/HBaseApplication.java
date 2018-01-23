package com.xinhuamm.xinhuasdk.base;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.luck.picture.lib.PictrureMediaLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.xinhuamm.xinhuasdk.BuildConfig;
import com.xinhuamm.xinhuasdk.PictrureGlideLoader;
import com.xinhuamm.xinhuasdk.R;
import com.xinhuamm.xinhuasdk.base.delegate.AppDelegate;
import com.xinhuamm.xinhuasdk.base.delegate.AppLifecycles;
import com.xinhuamm.xinhuasdk.di.component.AppComponent;
import com.xinhuamm.xinhuasdk.imageloader.loader.ImageLoader;
import com.xinhuamm.xinhuasdk.utils.HToast;

/**
 * 大家都要继承这个application，
 * 本项目由
 * mvp
 * +dagger2
 * +retrofit
 * +rxjava
 * +androideventbus
 * +butterknife组成
 * 请配合官方wiki文档https://github.com/JessYanCoding/MVPArms/wiki,学习本框架
 */
public class HBaseApplication extends android.support.multidex.MultiDexApplication implements App {

    private static HBaseApplication INSTANCE;

    private AppLifecycles mAppDelegate;

    //static 代码段可以防止内存泄露

    /**
     * 1.这样设置的Header和Footer的优先级是最低的
     * 2.XML设置的Header和Footer的优先级是中等的，会被方法三覆盖。
     * 3.Java代码设置，参考：设置 Header 为 Material风格
     refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
     设置 Footer 为 球脉冲
     refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    public static HBaseApplication getInstance() {
        return INSTANCE;
    }

    /**
     * 这里会在 {@link HBaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate.onCreate(this);
        INSTANCE = this;
        init();
    }

    private void init() {
        // 多进程导致多次初始化Application,这里只初始化App主进程的Application
//        String curProcessName = DeviceUtils.getProcessName(this, android.os.Process.myPid());
        //图片选择器
        PictrureMediaLoader.getInstance().init(new PictrureGlideLoader());
        //初始化吐司
        HToast.init(getApplicationContext());
        ImageLoader.init(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        //腾讯Bugly初始化
        CrashReport.initCrashReport(this, BuildConfig.BUGLY_APPID, false);
    }


    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return ((App) mAppDelegate).getAppComponent();
    }

}
