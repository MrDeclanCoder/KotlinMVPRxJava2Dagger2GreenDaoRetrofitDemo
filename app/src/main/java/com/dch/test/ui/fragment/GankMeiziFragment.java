package com.dch.test.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dch.test.R;
import com.dch.test.adapter.swipemenu.MenuVerticalAdapter;
import com.dch.test.adapter.swipemenu.OnItemClickListener;
import com.dch.test.base.BaseApplication;
import com.dch.test.base.BaseFragment;
import com.dch.test.contract.HomeContract;
import com.dch.test.di.activity.DaggerHomeActivityComponent;
import com.dch.test.di.activity.HomePresenterModule;
import com.dch.test.repository.entity.GankEntity;
import com.dch.test.ui.HomeActivity;
import com.dch.test.ui.KotlinPhotoViewActivity;
import com.dch.test.util.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Dch on 2017/4/13 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class GankMeiziFragment extends BaseFragment {

    private boolean loadMore = false;
    private List<GankEntity.Data> mData = new ArrayList<>();
    private HomeContract.HomePresenter homePresenter;

    private MeizhiAdapter dataAdapter;
    @BindView(R.id.swipeMenuRecyclerView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;

    @BindView(R.id.swiperefreshlayout_meizi)
    SwipeRefreshLayout swiperefreshlayout;
    private ArrayList<String> stringArrayList;

    public static GankMeiziFragment newInstance() {
        return new GankMeiziFragment();
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, null);
        ButterKnife.bind(this, rootView);
        initView();
        initInject();
        homePresenter.getMeiziData(1, 20);
        return rootView;
    }

    public void initInject() {
        DaggerHomeActivityComponent.builder()
                .homePresenterModule(new HomePresenterModule(this))
                .articalRepositoryComponent(((BaseApplication) activity.getApplication()).getArticalRepositoryComponent())
                .build().inject((HomeActivity) getActivity());
    }

    private void initView() {
        swiperefreshlayout.setColorSchemeColors(getResources().getColor(R.color.red_normal),
                getResources().getColor(R.color.green_normal),
                getResources().getColor(R.color.purple_normal));
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int nextInt = new Random().nextInt(10);
                homePresenter.getMeiziData(nextInt,20);
            }
        });
        swipeMenuRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                swipeRightMenu.setOrientation(SwipeMenu.VERTICAL);
                SwipeMenuItem swipeMenuItem = new SwipeMenuItem(activity)
                        .setBackgroundColor(R.color.colorPrimary)
                        .setImage(android.R.drawable.btn_star_big_off)
                        .setText("收藏")
                        .setTextColor(Color.WHITE)
                        .setWidth(100)
                        .setHeight(0)
                        .setWeight(1);
                swipeRightMenu.addMenuItem(swipeMenuItem);
            }
        };
        ArrayList<String> mDataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDataList.add("我是第" + i + "个菜单");
        }

        MenuVerticalAdapter menuVerticalAdapter = new MenuVerticalAdapter(mDataList);
        menuVerticalAdapter.setOnItemClickListener(onItemClickListener);

        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
    }

    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

            Intent intent = new Intent(getActivity(), KotlinPhotoViewActivity.class);
            intent.putExtra("pos",position);
            intent.putStringArrayListExtra("imglist",stringArrayList);
            getActivity().startActivity(intent);
        }
    };

    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {

        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                //右侧点击
                //收藏操作逻辑
                ToastUtils.showMessage("点击了 " + menuPosition);
            }
        }
    };


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
        if (loadMore) {
            List<GankEntity.Data> results = gankEntity.results;
            mData.addAll(results);
        } else {
            mData.clear();
            mData = gankEntity.results;
            if (dataAdapter == null) {
                dataAdapter = new MeizhiAdapter();
                swipeMenuRecyclerView.setAdapter(dataAdapter);
            } else {
                dataAdapter.notifyDataSetChanged();
            }
            swiperefreshlayout.setRefreshing(false);
            stringArrayList = new ArrayList<>();
            for(GankEntity.Data entity: mData){
                stringArrayList.add(entity.url);
            }
        }
    }

    @Override
    public void showError(Throwable throwable) {
    }

    private class MeizhiAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(GankMeiziFragment.this.getActivity(), R.layout.item_meizi, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            GankEntity.Data data = mData.get(position);
            try {
                Glide.with(GankMeiziFragment.this.getActivity()).load(data.url).centerCrop().placeholder(R.drawable.guide4).into(holder.iv_item_gank);
                holder.iv_item_gank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), KotlinPhotoViewActivity.class);
                        intent.putExtra("pos",position);
                        intent.putStringArrayListExtra("imglist",stringArrayList);
                        getActivity().startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_item_gank;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item_gank = (ImageView) itemView.findViewById(R.id.item_meizi_iv);
        }
    }

}
