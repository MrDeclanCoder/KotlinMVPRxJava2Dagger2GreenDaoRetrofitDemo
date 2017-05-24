package com.dch.test.base;

/**
 * 作者：Dch on 2017/4/10 15:53
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public interface BaseView<T> {
    void setHomePresenter(T homePresenter);
    void showLoading();
    void hideLoading();
    void showError(String error);
}
