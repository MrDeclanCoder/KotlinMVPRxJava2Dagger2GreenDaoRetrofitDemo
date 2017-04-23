package com.dch.test.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dch.test.R;
import com.dch.test.base.BaseFragment;
import com.dch.test.base.adapter.ListBaseAdapter;
import com.dch.test.base.adapter.SuperViewHolder;
import com.dch.test.contract.HomeContract;
import com.dch.test.repository.entity.GankEntity;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Dch on 2017/4/13 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class CsdnBlogFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private boolean loadMore = false;
    private List<String> mData = new ArrayList<>();
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private DataAdapter<String> mDataAdapter;
    private HomeContract.Presenter presenter;

    @BindView(R.id.recyclerview)
    LRecyclerView mRecyclerView;

    public static CsdnBlogFragment newInstance() {
        return new CsdnBlogFragment();
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1, null);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mDataAdapter = new DataAdapter<>(activity);
        mDataAdapter.setDataList(mData);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setOnRefreshListener(this);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
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
        lRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.refreshComplete(mDataAdapter.getItemCount());
    }

    @Override
    public void showError(Throwable throwable) {
    }

    @Override
    public void onRefresh() {
        loadMore = false;
    }
    @Override
    public void onLoadMore() {
//        presenter.getArticalsData();
    }

    private class DataAdapter<String> extends ListBaseAdapter<String> {


        public DataAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_csdn;
        }

        @Override
        protected void onBindItemHolder(SuperViewHolder holder, int position) {
            TextView textView = holder.getView(R.id.tv_item_title);
            textView.setText(mDataList.get(position).toString());
        }
    }
}
