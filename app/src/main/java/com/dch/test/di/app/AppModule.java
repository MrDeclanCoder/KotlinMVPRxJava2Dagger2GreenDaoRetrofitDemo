package com.dch.test.di.app;

import android.content.Context;

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
public final class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideApplicationContext() {
        return context;
    }

    @Provides
    CsdnApiService provideCsdnApiService(RetrofitManager retrofitManager) {
        return retrofitManager.createCsdnApiService();
    }

    @Provides
    GankApiService provideGankApiService(RetrofitManager retrofitManager) {
        return retrofitManager.createGankApiService();
    }

    @Provides
    SpfManager provideSpfManager() {
        return new SpfManager(context);
    }

    @Provides
    DBManager provideDBManager() {
        return new DBManager(context);
    }
}
