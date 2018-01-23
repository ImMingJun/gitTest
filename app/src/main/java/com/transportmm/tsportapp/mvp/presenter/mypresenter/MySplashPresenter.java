package com.transportmm.tsportapp.mvp.presenter.mypresenter;

import android.app.Application;
import android.util.Log;

import com.transportmm.tsportapp.mvp.contract.mycontract.MySplashContract;
import com.transportmm.tsportapp.mvp.model.entity.MySplashResult;
import com.xinhuamm.xinhuasdk.di.scope.FragmentScope;
import com.xinhuamm.xinhuasdk.integration.AppManager;
import com.xinhuamm.xinhuasdk.mvp.BasePresenter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/9/21.
 */

@FragmentScope
public class MySplashPresenter extends BasePresenter<MySplashContract.Model, MySplashContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private AppManager mAppManager;

    @Inject
    public MySplashPresenter(MySplashContract.Model model, MySplashContract.View rootView
            , RxErrorHandler handler, Application application
            , AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    public void initDatas() {
        mModel.init()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(s -> {
                    List<MySplashResult> list = new ArrayList<MySplashResult>();
                    Document document = DocumentHelper.parseText(s);//将字符串转换成xml文档
                    Element rootElement = document.getRootElement();//开始解析
                    List childElements= rootElement.elements("city");
                    for (Object childElement : childElements) {//遍历子节点
                        MySplashResult result = new MySplashResult();
                        Element element = (Element) childElement;
                        result.setQuName(element.attribute("quName").getValue());
                        result.setCityName(element.attribute("cityname").getValue());//若参数写错，解析会卡死在这一步
                        result.setStateDetailed(element.attribute("stateDetailed").getValue());
                        result.setTem1(element.attribute("tem1").getValue());
                        result.setTem2(element.attribute("tem2").getValue());
                        result.setWindState(element.attribute("windState").getValue());
                        list.add(result);
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MySplashResult>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<MySplashResult> mySplashResults) {
                        mRootView.showMySplashResult(mySplashResults);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("flag---", "onComplete: onComplete" );
                    }
                });
    }
}
