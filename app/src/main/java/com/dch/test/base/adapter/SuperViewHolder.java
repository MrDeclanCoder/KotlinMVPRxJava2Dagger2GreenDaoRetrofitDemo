package com.dch.test.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by dch on 2017/4/16.
 */

public class SuperViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;

    public SuperViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId){
        View view = views.get(viewId);
        if(null == view){
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T) view;
    }
}
