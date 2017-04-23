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
public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final ArticalRepository mArticalRepository;

    @Inject
    public HomePresenter(HomeContract.View view, ArticalRepository articalRepository) {
        this.view = checkNotNull(view, "view不能为空");
        mArticalRepository = checkNotNull(articalRepository, "androidRepository不能为空");
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        getAndroidData();
    }

    @Override
    public void getAndroidData() {
        mArticalRepository.getAndroidData(new ArticalDataSource.GankCallback() {
            @Override
            public void onGankdataLoaded(GankEntity entity) {
                view.showDailyList(entity);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }
}
