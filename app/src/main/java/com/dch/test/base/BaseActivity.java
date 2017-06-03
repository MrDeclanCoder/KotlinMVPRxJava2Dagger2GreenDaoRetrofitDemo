package com.dch.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dch.test.R;
import com.dch.test.util.StatusBarUtils;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：${User} on 2017/4/10 16:26
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public abstract class BaseActivity extends AppCompatActivity{

    public Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
//                .statusBarColorTransform(R.color.colorPrimary)  //状态栏变色后的颜色
//                .init();
        setContentView(setLayoutId());
        unbinder = ButterKnife.bind(this);
//        initInject();
        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int setLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
