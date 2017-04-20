package com.dch.test.di.app;

import android.content.Context;

import com.dch.test.base.BaseApplication;
import com.dch.test.manager.DBManager;
import com.dch.test.manager.RetrofitManager;
import com.dch.test.manager.SpfManager;
import com.dch.test.repository.remote.apistores.CsdnApiService;
import com.dch.test.repository.remote.apistores.GankApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：Dch on 2017/4/20 14:18
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Module
public class AppModule {

    private BaseApplication baseApplication;

    public AppModule(BaseApplication baseApplication){
        this.baseApplication = baseApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return baseApplication;
    }

    @Provides
    @Singleton
    CsdnApiService provideCsdnApiService(RetrofitManager retrofitManager){
        return retrofitManager.createCsdnApiService();
    }

    @Provides
    @Singleton
    GankApiService provideGankApiService(RetrofitManager retrofitManager){
        return retrofitManager.createGankApiService();
    }

    @Provides
    @Singleton
    SpfManager provideSpfManager(){
        return new SpfManager(baseApplication);
    }

    @Provides
    @Singleton
    DBManager provideDBManager(){
        return new DBManager(baseApplication);
    }
}
