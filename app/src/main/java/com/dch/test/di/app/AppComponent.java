package com.dch.test.di.app;

import android.content.Context;

import com.dch.test.manager.DBManager;
import com.dch.test.manager.SpfManager;
import com.dch.test.repository.remote.apistores.CsdnApiService;
import com.dch.test.repository.remote.apistores.GankApiService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 作者：Dch on 2017/4/20 14:25
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context context(); //提供application的context

    CsdnApiService csdnApiService(); //csdn的网络请求

    GankApiService gankApiService(); //gank的网络请求

    SpfManager spfManager(); //SharePreference管理类

    DBManager dbManager(); //数据库管理类
}
