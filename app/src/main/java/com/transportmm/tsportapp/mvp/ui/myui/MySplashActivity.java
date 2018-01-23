package com.transportmm.tsportapp.mvp.ui.myui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.transportmm.tsportapp.R;
import com.transportmm.tsportapp.di.component.mycomponent.DaggerMySplashComponent;
import com.transportmm.tsportapp.di.module.mymodule.MySplashModule;
import com.transportmm.tsportapp.mvp.contract.mycontract.MySplashContract;
import com.transportmm.tsportapp.mvp.model.entity.MySplashResult;
import com.transportmm.tsportapp.mvp.presenter.mypresenter.MySplashPresenter;
import com.transportmm.tsportapp.mvp.ui.myui.adapter.MySplashRecycleViewAdapter;
import com.xinhuamm.xinhuasdk.base.activity.HBaseActivity;
import com.xinhuamm.xinhuasdk.di.component.AppComponent;
import com.xinhuamm.xinhuasdk.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

public class MySplashActivity extends HBaseActivity<MySplashPresenter> implements MySplashContract.View,View.OnClickListener {

    private List<MySplashResult> mySplashResults = new ArrayList<>();
    private MySplashRecycleViewAdapter mySplashRecycleViewAdapter;
    @BindView(R.id.mysplash_toolbar)
    Toolbar toolbar;
    @BindView(R.id.mysplash_recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.mysplash_floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMySplashComponent
                .builder()
                .appComponent(appComponent)
                .mySplashModule(new MySplashModule(this)) //请将MySplashModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mysplash;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        return super.initBundle(bundle);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange();
                toolbar.setAlpha(percent);
            }
        });
        toolbar.inflateMenu(R.menu.menu_mysplash_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getOrder()){
                    case 1:
                        Snackbar.make(toolbar.getRootView(), "设置", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 2:
                        Snackbar.make(toolbar.getRootView(), "options", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 3:
                        Snackbar.make(toolbar.getRootView(), item.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 4:
                        Snackbar.make(toolbar.getRootView(), item.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 5:
                        Snackbar.make(toolbar.getRootView(), item.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 6:
                        Snackbar.make(toolbar.getRootView(), item.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationIcon(R.mipmap.ic_back_normal);
        toolbar.getBackground().setAlpha(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "返回", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toolbar.setTitle("CoordinatorLayout");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mySplashRecycleViewAdapter = new MySplashRecycleViewAdapter(this,mySplashResults);
        recyclerView.setAdapter(mySplashRecycleViewAdapter);
        mPresenter.initDatas();
        floatingActionButton.setImageResource(R.mipmap.add2);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //以下代码可以处理内存被杀时，fragment恢复的问题，不至于多建几个fragment.
        //fragment_login改成你自己取的变量
//        mFragment = findFragment(MySplashFragment.class.getName());
//        if (mFragment == null) {
//            addFragment(R.id.fragment_login, MySplashFragment.newInstance(), MySplashFragment.class.getName());
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


    @Override
    public void showMySplashResult(List<MySplashResult> results) {
        int size = results.size();
        for (int i = 0;i<size;i++){
            mySplashResults.add(results.get(i));
        }
        mySplashRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mysplash_floatingActionButton:
                Snackbar.make(view, "菜单", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(MySplashActivity.this,MaterialActivity.class));
                break;

        }
    }
}
