package com.transportmm.tsportapp.mvp.ui.main.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.transportmm.tsportapp.R;
import com.transportmm.tsportapp.mvp.ui.account.activity.LoginActivity;
import com.transportmm.tsportapp.mvp.ui.main.decoration.StickySectionDecoration;
import com.transportmm.tsportapp.mvp.ui.main.entity.GroupInfo;
import com.xinhuamm.xinhuasdk.base.fragment.HBaseRecyclerViewFragment;
import com.xinhuamm.xinhuasdk.di.component.AppComponent;
import com.xinhuamm.xinhuasdk.imageloader.config.DiskCacheStrategyMode;
import com.xinhuamm.xinhuasdk.imageloader.config.ScaleMode;
import com.xinhuamm.xinhuasdk.imageloader.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by bill on 17/7/24.
 */

public class ManageFragment extends HBaseRecyclerViewFragment {

    String URL1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490944508&di=671845045c66356487c1a539c4ed0717&imgtype=jpg&er=1&src=http%3A%2F%2Fattach.bbs.letv.com%2Fforum%2F201606%2F27%2F185306g84m4gsxztvzxjt5.jpg";
    String URL2 = "http://s1.dwstatic.com/group1/M00/86/4A/81beb00a44bc52b4fdd46285de8f8f00.png";
    String URL3 = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2796659031,1466769776&fm=80&w=179&h=119&img.JPEG";
    String URL4 = "https://isparta.github.io/compare-webp/image/gif_webp/gif/1.gif";
    String URL5 = "https://p.upyun.com/docs/cloud/demo.jpg!/format/webp";
    String URL6 = "http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=7555ca6eb051f819f1700b4eef8466db/f9dcd100baa1cd113ecc3e65b912c8fcc3ce2d78.jpg";
    String URL7 = "http://img4.imgtn.bdimg.com/it/u=1116809896,910758313&fm=214&gp=0.gif";
    String URL8 = "http://q.qlogo.cn/qqapp/1101502222/1D578CECC61D2A88C6D804F99DE64DF1/100";

    String IMG_NAME = "SHARE_IMG2.PNG";
    String IMG_NAME_C = "jpeg_test.jpeg";
    private enum Item {
        Repast("餐饮美食-简单自定义Header-外边距magin", LoginActivity.class),
        Profile("个人中心-PureScrollMode-纯滚动模式", LoginActivity.class),
        Webview("网页引用-WebView", LoginActivity.class),
        FeedList("微博列表-智能识别", LoginActivity.class),
        Weibo("微博主页-CoordinatorLayout", LoginActivity.class),
        Banner("滚动广告-Banner", LoginActivity.class),
        QQBrowser("QQ浏览器-模拟QQ浏览器内核提示", LoginActivity.class),
        QQ1Browser("QQ浏览器-模拟QQ浏览器内核提示", LoginActivity.class),
        QQ2Browser("QQ浏览器-模拟QQ浏览器内核提示", LoginActivity.class)
        ;
        public String name;
        public Class<?> clazz;
        Item(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

    }

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
    }

    @Override
    public void initBundle(Bundle bundle) {
        super.initBundle(bundle);
    }

    @Override
    public void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        StickySectionDecoration.GroupInfoCallback callback = new StickySectionDecoration.GroupInfoCallback() {
            @Override
            public GroupInfo getGroupInfo(int position) {

                /**
                 * 分组逻辑，这里为了测试每5个数据为一组。大家可以在实际开发中
                 * 替换为真正的需求逻辑
                 */
                int groupId = position / 5;
                int index = position % 5;
                GroupInfo groupInfo = new GroupInfo(groupId,groupId+"");
                groupInfo.setGroupLength(5);
                groupInfo.setPosition(index);
                return groupInfo;
            }
        };
        mRecyclerView.addItemDecoration(new StickySectionDecoration(mContext,callback));
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected BaseQuickAdapter getRecyclerAdapter() {
        return new QuickAdapter();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        refreshlayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Movie> movies = new Gson().fromJson(JSON_MOVIES, new TypeToken<ArrayList<Movie>>() {}.getType());
                mAdapter.replaceData(movies);
                refreshlayout.finishRefresh();
            }
        },2000);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     *
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }

    public class QuickAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.list_item_movie);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Movie item) {
            viewHolder.setText(R.id.lmi_title, item.filmName)
                    .setText(R.id.lmi_actor, item.actors)
                    .setText(R.id.lmi_grade, item.grade)
                    .setText(R.id.lmi_describe, item.shortinfo);
//            Glide.with(mContext).load(item.picaddr).into((ImageView) viewHolder.getView(R.id.lmi_avatar));
//            ((App)mContext.getApplicationContext()).getAppComponent().imageLoader().loadImage(mContext, GlideImageConfig
//                    .builder()
//                    .url(item.picaddr)
//                    .imageView((ImageView) viewHolder.getView(R.id.lmi_avatar))
//                    .build());
//            ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
//                @Override
//                public void animate(View view) {
//                    view.setAlpha( 0f );
//
//                    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
//                    fadeAnim.setDuration( 5500 );
//                    fadeAnim.start();
//                }
//            };

            int position=viewHolder.getLayoutPosition();
            if(position==0){
                ImageLoader.with(mContext)
                        .res(R.drawable.refresh_loading)
//                        .animate(animationObject)
                        .placeHolder(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategyMode.SOURCE)
                        .scale(ScaleMode.CENTER_CROP)
//                        .asGif()
                        .into((ImageView) viewHolder.getView(R.id.lmi_avatar));
            }else  if(position==1){
                ImageLoader.with(mContext)
                        .url(URL5)
//                        .animate(animationObject)
                        .placeHolder(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .scale(ScaleMode.CENTER_CROP)
                        .into((ImageView) viewHolder.getView(R.id.lmi_avatar));
            }else  if(position==2){
                ImageLoader.with(mContext)
                        .url(URL7)
//                        .animate(animationObject)
                        .placeHolder(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .scale(ScaleMode.CENTER_CROP)
                        .into((ImageView) viewHolder.getView(R.id.lmi_avatar));
            }

            else  if(position==3){
                ImageLoader.with(mContext)
                        .url(URL7)
//                        .animate(animationObject)
                        .placeHolder(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .scale(ScaleMode.CENTER_CROP)
                        .blur(40)
                        .into((ImageView) viewHolder.getView(R.id.lmi_avatar));
            }

            else  if(position==4){
                ImageLoader.with(mContext)
                        .url(URL8)
//                        .animate(animationObject)
                        .placeHolder(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .scale(ScaleMode.CENTER_CROP)
                        .blur(40)
                        .asCircle()
                        .into((ImageView) viewHolder.getView(R.id.lmi_avatar));
            }


        }
    }

//    public class GlideImageLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            imageView.setImageResource(((BannerItem) path).pic);
//        }
//    }

    public static class Movie {
        public String actors;
        public String filmName;
        public String grade;
        public String info;
        public String picaddr;
        public String shortinfo;
    }

    public static String JSON_MOVIES = "[" +
            "{\"actors\":\"丹尼斯·威缇可宁|Emma|Nikki|Jiayao|Wang|Maggie|Mao|Gang-yun|Sa\",\"filmName\":\"神灵寨\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg\",\"releasedate\":\"2017-07-31\",\"shortinfo\":\"父亲忽病危 新娘真够黑\",\"type\":\"剧情|喜剧\"}," +
            "{\"actors\":\"刘亦菲|杨洋|彭子苏|严屹宽|罗晋\",\"filmName\":\"三生三世十里桃花\",\"grade\":\"9.2\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3566.jpg\",\"releasedate\":\"2017-08-03\",\"shortinfo\":\"虐心姐弟恋 颜值要逆天\",\"type\":\"剧情|爱情|奇幻\"}," +
            "{\"actors\":\"尹航|代旭|李晨浩|衣云鹤|张念骅\",\"filmName\":\"谁是球王\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3750.jpg\",\"releasedate\":\"2017-08-03\",\"shortinfo\":\"足球变人生 再战可辉煌\",\"type\":\"剧情|喜剧\"}," +
            "{\"actors\":null,\"filmName\":\"大象林旺之一夜成名\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3757.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"大象参二战 一生好伙伴\",\"type\":\"动作|动画|战争|冒险\"}," +
            "{\"actors\":\"薛凯琪|陈意涵|张钧甯|迈克·泰森\",\"filmName\":\"闺蜜2：无二不作\",\"grade\":\"8.3\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3776.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"闺蜜团出战 会一会新娘\",\"type\":\"喜剧|爱情\"}," +
            "{\"actors\":\"彭禺厶|王萌|周凯文|曹琦|孟子叶\",\"filmName\":\"诡井\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3824.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"午夜深井中 怨魂欲现形\",\"type\":\"恐怖|惊悚\"}," +
            "{\"actors\":\"旺卓措|刘承宙|高欣生|段楠|来钰\",\"filmName\":\"荒野加油站\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3821.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"夜半拉乘客 结果遇不测\",\"type\":\"惊悚|悬疑\"}," +
            "{\"actors\":\"刘佩琦|曹云金|罗昱焜\",\"filmName\":\"龙之战\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3778.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"持倭刀屹立 抗外敌救国\",\"type\":\"动作|战争|历史\"}," +
            "{\"actors\":\"金巴|曲尼次仁|夏诺.扎西敦珠|索朗尼玛|益西旦增\",\"filmName\":\"皮绳上的魂\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3801.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"走完朝圣路 又上降魔旅\",\"type\":\"剧情\"}," +
            "{\"actors\":\"严丽祯|李晔|王衡|李传缨|李心仪\",\"filmName\":\"玩偶奇兵\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3779.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"玩偶战数码 一头两个大\",\"type\":\"动画|冒险|奇幻\"}," +
            "{\"actors\":\"斯蒂芬·马布里|吴尊|何冰|郑秀妍|王庆祥\",\"filmName\":\"我是马布里\",\"grade\":\"0.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3810.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"吴尊助冠军 热血灌篮魂\",\"type\":\"剧情|运动\"}," +
            "{\"actors\":\"周鹏雨|穆建荣|陈泽帆|鹿露|宋星成\",\"filmName\":\"原罪的羔羊\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3802.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"古镇来戏班 往事不一般\",\"type\":\"悬疑\"}," +
            "{\"actors\":\"王大陆|张天爱|任达华|盛冠森|王迅\",\"filmName\":\"鲛珠传\",\"grade\":\"7.1\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3777.jpg\",\"releasedate\":\"2017-08-04\",\"shortinfo\":\"改编热IP 杠杠号召力\",\"type\":\"喜剧|动作|奇幻\"}," +
            "{\"actors\":\"成龙|罗伯特·雷德福\",\"filmName\":\"地球：神奇的一天\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3803.jpg\",\"releasedate\":\"2017-08-11\",\"shortinfo\":\"史诗纪录片 十年再相见\",\"type\":\"纪录片\"}," +
            "{\"actors\":\"刘德华|舒淇|杨祐宁|张静初|让·雷诺|曾志伟|沙溢\",\"filmName\":\"侠盗联盟\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3592.jpg\",\"releasedate\":\"2017-08-11\",\"shortinfo\":\"侠盗三剑客 越洋逃恐吓\",\"type\":\"动作|冒险\"}," +
            "{\"actors\":\"廖凡|李易峰|万茜|李纯|张国柱\",\"filmName\":\"心理罪\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3795.jpg\",\"releasedate\":\"2017-08-11\",\"shortinfo\":\"侦探两搭档 真相背后藏\",\"type\":\"悬疑|犯罪\"}," +
            "{\"actors\":\"徐瑞阳|赵倩|姜启杨|徐万学|韩靓|韦安\",\"filmName\":\"隐隐惊马槽之绝战女僵尸\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3825.jpg\",\"releasedate\":\"2017-08-11\",\"shortinfo\":\"阴兵来借道 尸占惊马槽\",\"type\":\"惊悚|动作|冒险|悬疑\"}," +
            "{\"actors\":\"宋睿|王良|张佳浩|叶常清\",\"filmName\":\"左眼阴阳\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3804.jpg\",\"releasedate\":\"2017-08-11\",\"shortinfo\":\"左眼见到鬼 是诡还是魅\",\"type\":\"恐怖|惊悚|悬疑\"}," +
            "{\"actors\":null,\"filmName\":\"二十二\",\"grade\":\"10.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3811.jpg\",\"releasedate\":\"2017-08-14\",\"shortinfo\":\"二战女俘虏 讲述心中苦\",\"type\":\"纪录片\"}," +
            "{\"actors\":\"郭富城|王千源|刘涛|余皑磊|冯嘉怡\",\"filmName\":\"破·局\",\"grade\":\"5.0\",\"picaddr\":\"http://app.infunpw.com/commons/images/cinema/cinema_films/3812.jpg\",\"releasedate\":\"2017-08-18\",\"shortinfo\":\"影帝硬碰硬 迷局谁怕谁\",\"type\":\"动作|犯罪\"}" +
            "]";

}