package com.dch.test.contract;

import com.dch.test.base.BasePresenter;
import com.dch.test.base.BaseView;
import com.dch.test.contract.presenter.HomePresenter;

/**
 * 作者：Dch on 2017/4/10 15:56
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface HomeContract {

    interface HomeView extends BaseView<HomePresenter> {

        void showArticalList();

    }

    interface Presenter extends BasePresenter {
        void getArticalsData();
    }


}
