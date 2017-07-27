package com.dch.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dch.test.R;
import com.dch.test.contract.HomeContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Dch on 2017/4/11 12:04
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public abstract class BaseLoadLayoutFragment extends Fragment implements HomeContract.View{

    protected AppCompatActivity activity;
    protected View rootView;
    protected Unbinder unbinder;
    protected boolean isPrepared = false;

    @BindView(R.id.progressbar_base)
    protected ProgressBar progressbar_base;
    @BindView(R.id.ll_content_base)
    protected LinearLayout ll_content_base;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ((AppCompatActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_base_loadlayout,null);
        }
        if (null != rootView) {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeAllViews();
            }
            unbinder = ButterKnife.bind(rootView, activity);
        }

        ll_content_base.addView(addContentView(inflater));
        isPrepared = true;
        return rootView;
    }

    protected abstract View addContentView(LayoutInflater inflater);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
        if (null != unbinder)
            unbinder.unbind();
    }
}
