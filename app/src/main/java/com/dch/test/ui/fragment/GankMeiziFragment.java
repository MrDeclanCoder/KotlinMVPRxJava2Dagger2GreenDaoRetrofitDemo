package com.dch.test.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import com.dch.test.util.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

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
    private HomeContract.Presenter presenter;

    private MeizhiAdapter dataAdapter;
    @BindView(R.id.swipeMenuRecyclerView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;

    public static GankMeiziFragment newInstance() {
        return new GankMeiziFragment();
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, null);
        ButterKnife.bind(this, rootView);
        initView();
        initInject();
        presenter.getAndroidData(1,20);
        return rootView;
    }

    public void initInject() {
        DaggerHomeActivityComponent.builder()
                .homePresenterModule(new HomePresenterModule(this))
                .articalRepositoryComponent(((BaseApplication) activity.getApplication()).getArticalRepositoryComponent())
                .build().inject((HomeActivity) getActivity());
    }

    private void initView() {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
        }
    };

    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener(){

        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION){
                //右侧点击
                //收藏操作逻辑
                ToastUtils.showMessage("点击了 "+menuPosition);
            }
        }
    };


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
            List<GankEntity.Data> results = gankEntity.results;
            mData.addAll(results);
        } else {
            mData.clear();
            mData = gankEntity.results;
            dataAdapter = new MeizhiAdapter();
            swipeMenuRecyclerView.setAdapter(dataAdapter);
        }
    }

    @Override
    public void showError(Throwable throwable) {
    }

    private class MeizhiAdapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(activity,R.layout.item_gank,null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GankEntity.Data data = mData.get(position);
            holder.tv_item_title_gank.setText(data.type);
            holder.tv_item_time_gank.setText(data.createdAt.substring(0, 10));
            holder.tv_item_content_gank.setText(data.desc);
            try {
                Glide.with(activity).load(data.images[position]).fitCenter().placeholder(R.mipmap.ic_launcher).into(holder.iv_item_gank);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_item_title_gank;
        TextView tv_item_time_gank;
        TextView tv_item_content_gank;
        ImageView iv_item_gank;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_title_gank = (TextView) itemView.findViewById(R.id.tv_item_title_gank);
            tv_item_time_gank = (TextView) itemView.findViewById(R.id.tv_item_time_gank);
            tv_item_content_gank = (TextView) itemView.findViewById(R.id.tv_item_content_gank);
            iv_item_gank = (ImageView) itemView.findViewById(R.id.iv_item_gank);
        }
    }

}
