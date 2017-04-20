package com.dch.test.di;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：Dch on 2017/4/20 17:49
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Module
public class HomeModule {

    private final Fragment fragment;

    public HomeModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @ActivityScope
    Fragment fragment() {
        return this.fragment;
    }
}
