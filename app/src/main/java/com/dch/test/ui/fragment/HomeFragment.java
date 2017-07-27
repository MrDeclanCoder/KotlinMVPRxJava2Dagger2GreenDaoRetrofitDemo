package com.dch.test.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dch.test.base.BaseLoadLayoutFragment;
import com.dch.test.contract.HomeContract;
import com.dch.test.repository.entity.GankEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Dch on 2017/4/13 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class HomeFragment extends BaseLoadLayoutFragment implements SwipeRefreshLayout.OnRefreshListener{
    private boolean loadMore = false;
    private List<String> mData = new ArrayList<>();
    private HomeContract.HomePresenter homePresenter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View addContentView(LayoutInflater inflater) {
        mSwipeRefreshLayout = new SwipeRefreshLayout(getContext());
        mRecyclerView = new RecyclerView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefreshLayout.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams swipeRefreshLayoutParams = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(swipeRefreshLayoutParams);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return mSwipeRefreshLayout;
    }

    public void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showDailyList(GankEntity gankEntity) {
//        if (loadMore) {
//            mData = gankEntity.results;
//            mDataAdapter.addAll(gankEntity.results);
//        } else {
//            mDataAdapter.clear();
//            mData.clear();
//            mData = gankEntity.results;
//            mDataAdapter.setDataList(mData);
//        }
        progressbar_base.setVisibility(View.GONE);
        ll_content_base.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
    }

    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        loadMore = false;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (isPrepared){
                autoRefresh();
            }
        }
    }

    private void autoRefresh() {
        if (mSwipeRefreshLayout != null) {
            progressbar_base.setVisibility(View.VISIBLE);
//            homePresenter.getMeiziData();

        }
    }
}
