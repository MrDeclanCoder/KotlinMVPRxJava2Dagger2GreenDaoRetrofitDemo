package com.dch.test.ui.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.base.BaseApplication;
import com.dch.test.base.BaseFragment;
import com.dch.test.base.adapter.ListBaseAdapter;
import com.dch.test.base.adapter.SuperViewHolder;
import com.dch.test.contract.HomeContract;
import com.dch.test.di.activity.DaggerHomeActivityComponent;
import com.dch.test.di.activity.HomePresenterModule;
import com.dch.test.entity.MyFavorite;
import com.dch.test.repository.entity.GankEntity;
import com.dch.test.ui.DetailActivity;
import com.dch.test.ui.HomeActivity;
import com.dch.test.util.Config;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Dch on 2017/4/13 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class GankAndroidFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private boolean loadMore = false;
    private List<GankEntity.Data> mData = new ArrayList<>();
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private DataAdapter<GankEntity.Data> mDataAdapter;
    private HomeContract.Presenter presenter;

    public static String[] titles = new String[]{
            "每周7件Tee不重样",
            "俏皮又知性 适合上班族的漂亮衬衫",
            "名侦探柯南",
            "境界之轮回",
            "我的英雄学院",
            "全职猎人",
    };
    public static String[] urls = new String[]{//750x500
            "https://s2.mogucdn.com/mlcdn/c45406/170422_678did070ec6le09de3g15c1l7l36_750x500.jpg",
            "https://s2.mogucdn.com/mlcdn/c45406/170420_1hcbb7h5b58ihilkdec43bd6c2ll6_750x500.jpg",
            "http://s18.mogucdn.com/p2/170122/upload_66g1g3h491bj9kfb6ggd3i1j4c7be_750x500.jpg",
            "http://s18.mogucdn.com/p2/170204/upload_657jk682b5071bi611d9ka6c3j232_750x500.jpg",
            "http://s16.mogucdn.com/p2/170204/upload_56631h6616g4e2e45hc6hf6b7g08f_750x500.jpg",
            "http://s16.mogucdn.com/p2/170206/upload_1759d25k9a3djeb125a5bcg0c43eg_750x500.jpg"};

    @BindView(R.id.recyclerview)
    LRecyclerView mRecyclerView;

    public static GankAndroidFragment newInstance() {
        return new GankAndroidFragment();
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1, null);
        ButterKnife.bind(this, rootView);
        initInject();
        initView();
        return rootView;
    }
    public void initInject() {
        DaggerHomeActivityComponent.builder()
                .homePresenterModule(new HomePresenterModule(this))
                .articalRepositoryComponent(((BaseApplication) activity.getApplication()).getArticalRepositoryComponent())
                .build().inject((HomeActivity) getActivity());
    }

    @TargetApi(21)
    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mDataAdapter = new DataAdapter<>(activity);
        mDataAdapter.setDataList(mData);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.TriangleSkewSpin);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.forceToRefresh();
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(activity, DetailActivity.class);
                MyFavorite favorite = new MyFavorite();
                favorite.setTitle("Android");
                favorite.setContentDiscription(mData.get(position).desc);
                favorite.setUrl(mData.get(position).url);
                favorite.setFavoriteId(mData.get(position)._id);
                try {
                    favorite.setImgUrl(mData.get(position).images[0]);
                } catch (Exception e) {
                    Snackbar.make(mRecyclerView, "未获取到图片url", Snackbar.LENGTH_SHORT).show();
                    favorite.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494938304958&di=854d61b39d45b938505f573f6be7322f&imgtype=0&src=http%3A%2F%2Ff5.topit.me%2F5%2Fff%2F7e%2F11774047714a97eff5o.jpg");
                }finally {
                    i.putExtra(Config.MYFAVOTITE,favorite);
                    View sharedView = view.findViewById(R.id.iv_item_gank);
                    String transitionName = getString(R.string.transitionName);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, transitionName);
                    startActivity(i, transitionActivityOptions.toBundle());
                }
            }
        });
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
        if (loadMore) {
            mData.addAll(gankEntity.results);
            mDataAdapter.addAll(gankEntity.results);
        } else {
            mDataAdapter.clear();
            mData.clear();
            mData = gankEntity.results;
            mDataAdapter.setDataList(mData);
        }
        lRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.refreshComplete(mDataAdapter.getItemCount());
    }

    @Override
    public void showError(Throwable throwable) {
    }

    @Override
    public void onRefresh() {
        loadMore = false;
        presenter.getAndroidData(1, 20);
    }

    @Override
    public void onLoadMore() {
        loadMore = true;
        presenter.getAndroidData(2, 20);
    }

    private class DataAdapter<Data> extends ListBaseAdapter<GankEntity.Data> {


        public DataAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_gank;
        }

        @Override
        protected void onBindItemHolder(SuperViewHolder holder, int position) {
            GankEntity.Data data = mDataList.get(position);
            TextView textViewTitle = holder.getView(R.id.tv_item_title_gank);
            textViewTitle.setText(data.type);
            TextView textViewTime = holder.getView(R.id.tv_item_time_gank);
            textViewTime.setText(data.createdAt.substring(0, 10));
            TextView textViewContent = holder.getView(R.id.tv_item_content_gank);
            textViewContent.setText(data.desc);
            try {
                ImageView imageView = holder.getView(R.id.iv_item_gank);
                Glide.with(activity).load(data.images[0]).fitCenter().placeholder(R.drawable.guide4).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
