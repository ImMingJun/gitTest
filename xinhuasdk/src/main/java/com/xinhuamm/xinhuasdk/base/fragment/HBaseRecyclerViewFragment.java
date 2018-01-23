package com.xinhuamm.xinhuasdk.base.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xinhuamm.xinhuasdk.R;
import com.xinhuamm.xinhuasdk.mvp.IPresenter;
import com.xinhuamm.xinhuasdk.smartrefresh.RecyclerMode;

public abstract class HBaseRecyclerViewFragment<p extends IPresenter> extends HBaseTitleFragment<p> implements OnRefreshLoadmoreListener,
        BaseQuickAdapter.OnItemClickListener {

    protected int mPage = 1;//当前的页码
    protected boolean isRefresh = true;//默认是下拉刷新数据

    protected RefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;
    protected RecyclerMode mRecyclerMode = RecyclerMode.BOTH;//默认可以下拉和上拉

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);

        mTitleBar.setVisibility(View.GONE);//默认标题不显示
        mViewDivider.setVisibility(View.GONE);//默认不显示顶部阴影

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        mAdapter = getRecyclerAdapter();
        mAdapter.setOnItemClickListener(this);

        RecyclerView.ItemDecoration decoration = getDividerItemDecoration();
        if (decoration != null)
            mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);

        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshLoadmoreListener(this);
            setRecylerMode(mRecyclerMode);
        }
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    protected RecyclerView.ItemDecoration getDividerItemDecoration() {
        return new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
    }

    protected boolean isHasMoreInStickHead() {
        return false;
    }

    protected abstract BaseQuickAdapter getRecyclerAdapter();


    protected void setRecylerMode(RecyclerMode mode) {
        mRecyclerMode = mode;
        if (mRecyclerMode == RecyclerMode.BOTH) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadmore(true);
        } else if (mRecyclerMode == RecyclerMode.TOP) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadmore(false);
        } else if (mRecyclerMode == RecyclerMode.BOTTOM) {
            mRefreshLayout.setEnableRefresh(false);
            mRefreshLayout.setEnableLoadmore(true);
        } else {
            mRefreshLayout.setEnableRefresh(false);
            mRefreshLayout.setEnableLoadmore(false);
        }
    }

    @Override
    protected void onClickEmptyLayout() {
        onRefresh(mRefreshLayout);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }


    /**
     * 加载更多
     *
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isRefresh = false;
        mPage++;
    }

    /**
     * 下拉刷新
     *
     * @param refreshlayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isRefresh = true;
        mPage = 1;
    }
}
