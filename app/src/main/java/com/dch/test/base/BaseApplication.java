package com.dch.test.base;

import android.support.multidex.MultiDexApplication;

import com.dch.test.di.app.AppModule;
import com.dch.test.repository.ArticalRepositoryComponent;
import com.dch.test.repository.DaggerArticalRepositoryComponent;
import com.dch.test.util.CrashHandler;

/**
 * 作者：${User} on 2017/4/10 16:41
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BaseApplication extends MultiDexApplication {
    public static BaseApplication application;
    private ArticalRepositoryComponent articalRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        articalRepositoryComponent = DaggerArticalRepositoryComponent.builder()
                .appModule(new AppModule((getApplicationContext())))
                .build();
    }

    public ArticalRepositoryComponent getArticalRepositoryComponent(){
        return articalRepositoryComponent;
    }
}
