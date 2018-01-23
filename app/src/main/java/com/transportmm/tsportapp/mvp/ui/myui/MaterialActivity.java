package com.transportmm.tsportapp.mvp.ui.myui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.transportmm.tsportapp.R;
import com.transportmm.tsportapp.di.component.DaggerMaterialComponent;
import com.transportmm.tsportapp.di.module.MaterialModule;
import com.transportmm.tsportapp.mvp.contract.MaterialContract;
import com.transportmm.tsportapp.mvp.presenter.MaterialPresenter;
import com.xinhuamm.xinhuasdk.base.activity.HBaseActivity;
import com.xinhuamm.xinhuasdk.di.component.AppComponent;
import com.xinhuamm.xinhuasdk.imageloader.loader.ImageLoader;
import com.xinhuamm.xinhuasdk.utils.UiUtils;

import butterknife.BindView;
import butterknife.Unbinder;

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
 * Created by Administrator on 2017/12/25.
 */

public class MaterialActivity extends HBaseActivity<MaterialPresenter> implements MaterialContract.View {

    //    @BindView(R.id.rb_user_attention_care)
//    RoundButton rbAttention;
    @BindView(R.id.iv_home_page_head_bg)
    ImageView ivHomePageHeadBg;
    @BindView(R.id.iv_home_page_middle_head)
    ImageView ivHomePageMiddleHead;
    @BindView(R.id.iv_home_page_vip)
    ImageView ivHomePageVip;
    //    @BindView(R.id.tv_user_attention_nick)
//    TextView tvUserAttentionNick;
//    @BindView(R.id.tv_attention_desc)
//    TextView tvAttentionDesc;
//    @BindView(R.id.tv_home_page_manuscript_count)
//    TextView tvHomePageManuscriptCount;
//    @BindView(R.id.tv_home_page_attention_count)
//    TextView tvHomePageAttentionCount;
//    @BindView(R.id.tv_home_page_fans_count)
//    TextView tvHomePageFansCount;
    @BindView(R.id.appbar_user_home_page)
    AppBarLayout appbarUserHomePage;
    @BindView(R.id.iv_home_page_share)
    ImageView ivHomePageShare;
    //    @BindView(R.id.report_title_hint)
//    TextView titleHint;
    @BindView(R.id.toolbar_home_page)
    Toolbar mToolbar;
    @BindView(R.id.iv_home_page_collapsed_head)
    ImageView ivHomePageCollapsedHead;

//    @BindView(R.id.tv_home_page_collapsed_nick)
//    TextView tvHomePageCollapsedNick;

    @BindView(R.id.ll_home_page_toolbar)
    LinearLayout mLlToolBar;
    //    @BindView(R.id.rl_home_page_fragment)
//    LinearLayout linearLayout;
//    @BindView(R.id.rb_user_page_collapsed_attention)
//    RoundButton mRbtnCollapsedAttention;
    @BindView(R.id.listview)
    ListView listView;

    private Unbinder unBinder;
//    private UserHomeInfoBean mHomeInfo;

    private TabLayoutOffsetChangeListener mOffsetChangerListener;
//    private JiZheCenterFragment jzFragment;

    private String userId = null;  // 登录记者or 指定记者的id

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMaterialComponent
                .builder()
                .appComponent(appComponent)
                .materialModule(new MaterialModule(this)) //请将MaterialModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_material;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        return super.initBundle(bundle);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        mToolbar.setTitle("");
        mToolbar.setSubtitle("");
//        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                overridePendingTransition(0, R.anim.push_right_out);
            }
        });

        Bundle args2 = new Bundle();
        args2.putString("id", userId);
//        jzFragment = new JiZheCenterFragment();
//        jzFragment.setArguments(args2);
//        getSupportFragmentManager().beginTransaction().replace(R.id.rl_home_page_fragment, jzFragment,
//                JiZheCenterFragment.class.getName()).commit();

//        if (MainApplication.getInstance().getUserId().equals(userId)) {
//            titleHint.setText("我的报道");
//        }
//        else {
//            titleHint.setText("TA的报道");
//        }
        ImageLoader.with(this)
                .res(R.mipmap.voice_seek_thumb)
                .blur(70).into(ivHomePageCollapsedHead);
        ImageLoader.with(this)
                .res(R.mipmap.voice_pause_normal)
                .blur(70).into(ivHomePageHeadBg);
        ivHomePageHeadBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mOffsetChangerListener = new TabLayoutOffsetChangeListener();
        appbarUserHomePage.addOnOffsetChangedListener(mOffsetChangerListener);

        String[] list = new String[]{"姓名:张三", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号", "性别：男", "地址：重庆市沙坪坝区沙正街174号"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
//        for(int i = 0;i<20;i++){
//            TextView textView = new TextView(this);
//            textView.setText("aaaaaa");
//            linearLayout.addView(textView);
//        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //以下代码可以处理内存被杀时，fragment恢复的问题，不至于多建几个fragment.
        //fragment_login改成你自己取的变量
//        mFragment = findFragment(MaterialFragment.class.getName());
//        if (mFragment == null) {
//            addFragment(R.id.fragment_login, MaterialFragment.newInstance(), MaterialFragment.class.getName());
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


    private class TabLayoutOffsetChangeListener implements AppBarLayout.OnOffsetChangedListener {
        boolean isShow = false;
        int mScrollRange = -1;
        //当距离小于50dp的时候，就出现折叠栏的按钮，头像
        int MAX_DISTANCE = 50;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (mScrollRange == -1) {
                mScrollRange = appBarLayout.getTotalScrollRange();
            }
            if (mScrollRange + verticalOffset <= DensityUtil.dp2px((float) MAX_DISTANCE)) {
                if (!isShow) {
                    mLlToolBar.setVisibility(View.VISIBLE);
//                    setToolBarCollapsedAttention();
                    isShow = true;
                }
            } else if (isShow) {
                mLlToolBar.setVisibility(View.GONE);
                isShow = false;
            }
        }

        public void resetRange() {
            mScrollRange = -1;
        }
    }

    /**
     * 设置折叠后ToolBar的关注按钮的状态
     */
//    private void setToolBarCollapsedAttention() {
//
//        if (mHomeInfo == null)
//            return;
//
//        if (MainApplication.getInstance().getUserId().
//                equals(String.valueOf(mHomeInfo.getUserId())))
//            return;
//
//        mRbtnCollapsedAttention.setVisibility(View.VISIBLE);
//
//        if (mHomeInfo.getStates() == UserHomeInfoBean.TYPE_ATTENTION_NO) {//未关注
//            mRbtnCollapsedAttention.changeWholeColorByResId(R.color.v4_main_blue,
//                    R.dimen.v4_btn_border_width_normal,
//                    R.color.v4_main_blue,
//                    R.dimen.v4_btn_corner_radius_2dp,
//                    R.color.v4_main_white);
//            mRbtnCollapsedAttention.setText(R.string.attention);
//            //设置完毕后，必须主动调用update()方法
//            mRbtnCollapsedAttention.update();
//        }
//        else {//已经关注
//            mRbtnCollapsedAttention.changeWholeColorByResId(R.color.v4_main_blue,
//                    R.dimen.v4_btn_border_width_normal,
//                    R.color.all_transparent,
//                    R.dimen.v4_btn_corner_radius_2dp,
//                    R.color.v4_main_blue);
//            mRbtnCollapsedAttention.setText(R.string.already_attention);
//            //设置完毕后，必须主动调用update()方法
//            mRbtnCollapsedAttention.update();
//        }
//    }

    /**
     * 设置折叠钱head的关注按钮的状态
     */
//    private void setAttentionBtnState() {
//
//        if (mHomeInfo == null)
//            return;
//
//        if (MainApplication.getInstance().getUserId().
//                equals(String.valueOf(mHomeInfo.getUserId()))) {
//            rbAttention.setVisibility(View.GONE);
//            return;
//        }
//
//        rbAttention.setVisibility(View.VISIBLE);
//        if (mHomeInfo.getStates() == UserHomeInfoBean.TYPE_ATTENTION_NO) {//未关注
//            rbAttention.changeWholeColorByResId(R.color.v4_main_blue,
//                    R.dimen.v4_btn_border_width_normal,
//                    R.color.v4_main_blue,
//                    R.dimen.v4_btn_corner_radius_normal,
//                    R.color.v4_main_white);
//            rbAttention.setText(R.string.attention);
//            //设置完毕后，必须主动调用update()方法
//            rbAttention.update();
//        }
//        else {//已经关注
//            rbAttention.changeWholeColorByResId(R.color.v4_main_blue,
//                    R.dimen.v4_btn_border_width_normal,
//                    R.color.all_transparent,
//                    R.dimen.v4_btn_corner_radius_normal,
//                    R.color.v4_main_blue);
//            rbAttention.setText(R.string.already_attention);
//            //设置完毕后，必须主动调用update()方法
//            rbAttention.update();
//        }
//
//    }

//
//    @OnClick({R.id.rb_user_page_collapsed_attention, R.id.rb_user_attention_care})
//    public void onAttentionClick() {
//        if (mHomeInfo == null) {
//            ToastView.showShort("记者信息为空");
//            return;
//        }
//        if (MainApplication.application.getUserId() != null && MainApplication.application.getUserId().equals(mHomeInfo.getUserId())) {
//            ToastView.showShort("亲，您不能关注自己哦");
//            return;
//        }
//
//        if (XinhuaUtils.checkLoginState(UserHomePageActivity.this, true)) {
////            alertView.showProgress(R.string.status_sending);
//            mPresenter.userChangeAttention(mHomeInfo.getUserId());
//
//        }
//    }
//
//    @OnClick(R.id.iv_home_page_share)
//    public void onShareClick() {
//        if(null == mHomeInfo){
//            return;
//        }
//        String content = "";
//        String title = "推荐" + "『" + mHomeInfo.getUiName() + "』" + "的主页";
//        String summary = "";
//        String shareUrl = mHomeInfo.getShareurl();
//        String avatar = mHomeInfo.getUiHeadImage();
////        String shortUrl = "";
//
//        NewsDetailEntity detail = new NewsDetailEntity();
//        detail.setContent(content);
//        detail.setShareurl(shareUrl);
//        detail.setTopic(title);
//        detail.setShareImage(avatar);
//        detail.setId("");
//        detail.setNewstype(NewsTypeEnum.NewsType.REPORT_HOME_PAGE.getCode());
//
//        UmengShareUtil.getStance(this).shareNews(detail);
//    }
}
