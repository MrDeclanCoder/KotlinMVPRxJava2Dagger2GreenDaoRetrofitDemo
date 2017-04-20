package com.dch.test.di.activity;

import android.support.v7.app.AppCompatActivity;

import com.dch.test.base.BaseActivity;
import com.dch.test.di.app.AppComponent;
import com.dch.test.di.scope.ActivityScope;
import com.dch.test.ui.DetailActivity;
import com.dch.test.ui.GuideActivity;
import com.dch.test.ui.HomeActivity;
import com.dch.test.ui.SplashActivity;

import dagger.Component;

/**
 * 作者：Dch on 2017/4/20 14:45
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    AppCompatActivity activity();

    void inject(BaseActivity baseActivity);

    void inject(HomeActivity homeActivity);

    void inject(SplashActivity splashActivity);

    void inject(DetailActivity detailActivity);

    void inject(GuideActivity guideActivity);

}
