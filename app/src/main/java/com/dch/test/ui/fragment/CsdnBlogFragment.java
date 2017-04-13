package com.dch.test.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dch.test.Injection;
import com.dch.test.R;
import com.dch.test.base.BaseFragment;
import com.dch.test.contract.HomeContract;
import com.dch.test.contract.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Dch on 2017/4/13 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class CsdnBlogFragment extends BaseFragment implements HomeContract.HomeView  {
    private HomePresenter presenter;
    private List<String> mData = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    public static CsdnBlogFragment newInstance(){
        return new CsdnBlogFragment();
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1, null);
        ButterKnife.bind(this,rootView);
        presenter = new HomePresenter(this, Injection.provideArticalRepository(activity));
        presenter.start();
        initView();
        return rootView;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity,2));
    }


    @Override
    public void setPresenter(HomePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showArticalList(ArrayList<String> list) {
        for (String s : list) {
            System.out.println("获取到文章数据--" + s);
        }
        mData = list;
        myAdapter = new MyAdapter(mData);
        mRecyclerView.setAdapter(myAdapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private List<String> data;
        public MyAdapter(List<String> list){
            this.data = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(View.inflate(parent.getContext(),R.layout.item_recycler,null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTextView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = ((TextView) itemView.findViewById(R.id.tv_item_title));
        }
    }
}
