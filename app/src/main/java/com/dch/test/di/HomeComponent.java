package com.dch.test.di;

import android.support.v4.app.Fragment;

import com.dch.test.di.activity.ActivityModule;
import com.dch.test.di.app.AppComponent;
import com.dch.test.di.scope.FragmentScope;
import com.dch.test.repository.ArticalRepository;
import com.dch.test.repository.HomeRepositoryComponent;

import dagger.Component;

/**
 * 作者：Dch on 2017/4/20 17:48
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@FragmentScope
@Component(dependencies = HomeRepositoryComponent.class, modules = {HomePresenterModule.class, ActivityModule.class})
public interface HomeComponent {

    void inject(Fragment fragment);

}
