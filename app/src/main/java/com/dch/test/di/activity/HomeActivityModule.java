package com.dch.test.di.activity;

import android.support.v7.app.AppCompatActivity;

import com.dch.test.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：Dch on 2017/4/20 14:48
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Module
public class HomeActivityModule {

    private final AppCompatActivity activity;

    public HomeActivityModule(AppCompatActivity activity){
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    AppCompatActivity activity(){
        return this.activity;
    }

}
