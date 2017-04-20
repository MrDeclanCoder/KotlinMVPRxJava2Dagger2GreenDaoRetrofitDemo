package com.dch.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Dch on 2017/4/11 12:04
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public abstract class BaseFragment extends Fragment {

    protected AppCompatActivity activity;
    protected View rootView;
    protected Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ((AppCompatActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = initRootView(inflater, container, savedInstanceState);
        }
        if (null != rootView) {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeAllViews();
            }
            unbinder = ButterKnife.bind(rootView, activity);
        }
        return rootView;
    }


    protected abstract View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder)
            unbinder.unbind();
    }
}
