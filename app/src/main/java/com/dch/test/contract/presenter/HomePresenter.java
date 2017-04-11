package com.dch.test.contract.presenter;

import com.dch.test.contract.HomeContract;
import com.dch.test.repository.ArticalRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者：dch on 2017/4/10 16:32
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.HomeView homeView;
    private final ArticalRepository mArticalRepository;

    public HomePresenter(HomeContract.HomeView view, ArticalRepository articalRepository) {
        homeView = checkNotNull(view, "view不能为空");
        mArticalRepository = checkNotNull(articalRepository, "articalRepository不能为空");
        homeView.setPresenter(this);
    }

    @Override
    public void start() {
        getArticalsData();
    }

    @Override
    public void getArticalsData() {
        homeView.showArticalList();
    }
}
