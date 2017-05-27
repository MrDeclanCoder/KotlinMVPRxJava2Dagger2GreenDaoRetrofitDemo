package com.dch.test.contract.presenter;

import com.dch.test.contract.HomeContract;
import com.dch.test.repository.ArticalDataSource;
import com.dch.test.repository.ArticalRepository;
import com.dch.test.repository.entity.GankEntity;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者：dch on 2017/4/10 16:32
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class HomePresenter implements HomeContract.HomePresenter {
    private final HomeContract.View view;
    private final ArticalRepository mArticalRepository;

    @Inject
    public HomePresenter(HomeContract.View vieww, ArticalRepository articalRepository) {
        view = vieww;
        mArticalRepository = articalRepository;
    }

    @Inject
    void setupListeners(){
        view.setHomePresenter(this);
    }

    @Override
    public void start() {
        getAndroidData(1,20);
    }

    @Override
    public void getAndroidData(int pageNum, int pageSize) {
        mArticalRepository.getAndroidData(new ArticalDataSource.GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                view.showDailyList(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                view.showError(throwable);
            }
        },pageNum,pageSize);
    }

    @Override
    public void getMeiziData(int pageNum, int pageSize) {
        mArticalRepository.getMeiziData(new ArticalDataSource.GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                view.showDailyList(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                view.showError(throwable);
            }
        },pageNum,pageSize);
    }
}
