package com.dch.test.base;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.dch.test.di.app.AppModule;
import com.dch.test.repository.ArticalRepositoryComponent;
//import com.dch.test.repository.DaggerArticalRepositoryComponent;
import com.dch.test.repository.DaggerArticalRepositoryComponent;
import com.dch.test.util.CrashHandler;

import greendao.gen.DaoMaster;
import greendao.gen.DaoSession;

/**
 * 作者：${User} on 2017/4/10 16:41
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class BaseApplication extends MultiDexApplication {
    public static BaseApplication application;
    private ArticalRepositoryComponent articalRepositoryComponent;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase writableDatabase;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);

        articalRepositoryComponent = DaggerArticalRepositoryComponent.builder()
                .appModule(new AppModule((getApplicationContext())))
                .build();
        DatabaseInit();
    }

    private void DatabaseInit() {
        devOpenHelper = new DaoMaster.DevOpenHelper(this,"collect-db");
        writableDatabase = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(writableDatabase);
        daoSession = daoMaster.newSession();
    }

    public SQLiteDatabase getWritableDatabase() {
        return writableDatabase;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public ArticalRepositoryComponent getArticalRepositoryComponent(){
        return articalRepositoryComponent;
    }
}
