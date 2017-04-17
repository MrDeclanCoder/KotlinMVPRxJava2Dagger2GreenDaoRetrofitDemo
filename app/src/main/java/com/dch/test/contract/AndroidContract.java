package com.dch.test.contract;

import com.dch.test.base.BasePresenter;
import com.dch.test.base.BaseView;
import com.dch.test.contract.presenter.AndroidPresenter;
import com.dch.test.contract.presenter.CsdnBlogPresenter;
import com.dch.test.repository.entity.GankEntity;

import java.util.ArrayList;

/**
 * 作者：Dch on 2017/4/10 15:56
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface AndroidContract {

    interface AndroidView extends BaseView<AndroidPresenter> {

        void showAndroidDailyList(GankEntity entity);
        void showError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void getAndroidData();
    }


}
