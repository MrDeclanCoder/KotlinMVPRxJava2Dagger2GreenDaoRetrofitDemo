package com.dch.test.di;

import android.support.v4.app.Fragment;

import dagger.Component;

/**
 * 作者：Dch on 2017/4/20 17:48
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {HomeModule.class, ActivityModule.class})
public interface HomeComponent {

    Fragment fragment();

    void inject(Fragment fragment);

}
