package com.dch.test.di;

import com.dch.test.contract.HomeContract;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：Dch on 2017/4/20 18:59
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
@Module
public class HomePresenterModule {

    private final HomeContract.View mView;

    public HomePresenterModule(HomeContract.View view){
        this.mView = view;
    }

    @Provides
    HomeContract.View provideHomeContractView(){
        return this.mView;
    }
}
