package com.dch.test.contract;

import com.dch.test.base.BasePresenter;
import com.dch.test.base.BaseView;
import com.dch.test.contract.presenter.CsdnBlogPresenter;

import java.util.ArrayList;

/**
 * 作者：Dch on 2017/4/10 15:56
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface CsdnBlogContract {

    interface CsdnBlogView extends BaseView<CsdnBlogPresenter> {

        void showArticalList(ArrayList<String> list);

    }

    interface Presenter extends BasePresenter {
        void getArticalsData();
    }


}
