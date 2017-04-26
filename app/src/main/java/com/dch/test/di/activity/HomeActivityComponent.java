package com.dch.test.di.activity;

import com.dch.test.di.scope.ActivityScope;
import com.dch.test.repository.ArticalRepositoryComponent;
import com.dch.test.ui.HomeActivity;
import dagger.Component;

/**
 * 作者：Dch on 2017/4/20 14:45
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@ActivityScope
@Component(dependencies = ArticalRepositoryComponent.class,modules = HomePresenterModule.class)
public interface HomeActivityComponent {

    void inject(HomeActivity homeActivity);

}
