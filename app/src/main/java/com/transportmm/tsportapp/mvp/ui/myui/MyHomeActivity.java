package com.transportmm.tsportapp.mvp.ui.myui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.transportmm.tsportapp.di.component.mycomponent.DaggerMyHomeComponent;
import com.transportmm.tsportapp.di.module.mymodule.MyHomeModule;
import com.transportmm.tsportapp.mvp.contract.mycontract.MyHomeContract;
import com.transportmm.tsportapp.mvp.presenter.mypresenter.MyHomePresenter;
import com.xinhuamm.xinhuasdk.base.activity.HBaseActivity;
import com.xinhuamm.xinhuasdk.di.component.AppComponent;
import com.xinhuamm.xinhuasdk.utils.UiUtils;

import static com.xinhuamm.xinhuasdk.utils.Preconditions.checkNotNull;

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

public class MyHomeActivity extends HBaseActivity<MyHomePresenter> implements MyHomeContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyHomeComponent
                .builder()
                .appComponent(appComponent)
                .myHomeModule(new MyHomeModule(this)) //请将MyHomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        return super.initBundle(bundle);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //以下代码可以处理内存被杀时，fragment恢复的问题，不至于多建几个fragment.
        //fragment_login改成你自己取的变量
//        mFragment = findFragment(MyHomeFragment.class.getName());
//        if (mFragment == null) {
//            addFragment(R.id.fragment_login, MyHomeFragment.newInstance(), MyHomeFragment.class.getName());
//        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


}
