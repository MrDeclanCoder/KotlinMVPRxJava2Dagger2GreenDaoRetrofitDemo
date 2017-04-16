package com.dch.test.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dch on 2017/4/16.
 */

public abstract class ListBaseAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {

    protected Context mContext;
    private LayoutInflater mInflater;
    public  List<T> mDataList = new ArrayList<>();

   public ListBaseAdapter(Context context){
       mContext = context;
       mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

   protected abstract int getLayoutId();

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getLayoutId(), parent, false);
        return new SuperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        onBindItemHolder(holder,position);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()){
            onBindViewHolder(holder,position);
        } else {
            onBindItemHolder(holder,position,payloads);
        }
    }

    protected abstract void onBindItemHolder(SuperViewHolder holder, int position);
    public void onBindItemHolder(SuperViewHolder holder, int position, List<Object> payloads){

    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList(){
        return mDataList;
    }

    public void setDataList(Collection<T> list){
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }
    
    public void addAll(Collection<T> list){
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)){
            notifyItemRangeInserted(lastIndex,list.size());
        }
    }

    public void remove(int position){
        this.mDataList.remove(position);
        notifyItemRemoved(position);
        if (position != (getDataList().size())){
          notifyItemRangeChanged(position,this.mDataList.size() - position);
        }
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }
}
